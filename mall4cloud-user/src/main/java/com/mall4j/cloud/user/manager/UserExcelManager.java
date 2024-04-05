package com.mall4j.cloud.user.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.auth.dto.AuthAccountDTO;
import com.mall4j.cloud.api.auth.dto.AuthAccountUserDTO;
import com.mall4j.cloud.api.auth.dto.UserRegisterDTO;
import com.mall4j.cloud.api.auth.dto.UserRegisterExtensionDTO;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.user.vo.UserManagerVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.LevelTypeEnum;
import com.mall4j.cloud.user.constant.SexTypeEnum;
import com.mall4j.cloud.user.constant.UserStatusEnum;
import com.mall4j.cloud.user.dto.UserExcelDTO;
import com.mall4j.cloud.user.dto.UserManagerDTO;
import com.mall4j.cloud.user.listener.UserExcelListener;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserLevelService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.vo.UserExcelVO;
import com.mall4j.cloud.user.vo.UserLevelVO;
import com.mall4j.cloud.user.vo.UserVO;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 用户信息导入导出
 *
 * @author cl
 * @date 2021-05-11 09:33:25
 */
@Component
public class UserExcelManager {

    @Autowired
    private UserService userService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private ThreadPoolExecutor userThreadPoolExecutor;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountFeignClient accountFeignClient;

    /**
     * 定义excel中一个sheet最大的行数 一百万行数据, 必须是PageDTO.MAX_PAGE_SIZE 的整数倍
     */
    public static final long SHEET_MAX_ROWS = 1000000;
    public static final String[] LEVEL_TYPE_MODE = {LevelTypeEnum.ORDINARY_USER.value().toString(), LevelTypeEnum.PAY_USER.value().toString()};
    public static final String[] SEX_MODE = {SexTypeEnum.FEMALE.desc(), SexTypeEnum.MALE.desc()};
    public static final int LEVEL_TYPE_INDEX = 3;
    public static final int SEX_INDEX = 9;
    public static final String FORMAT = "yyyy-MM-dd";
    public static final int YEAR_MONTH_DAY_LENGTH = 10;
    /**
     * 账户username前缀
     */
    public static final String TEL_PREFIX = "Tel";

    private static final Logger log = LoggerFactory.getLogger(UserExcelManager.class);

    /**
     * 导出用户信息
     *
     * @param response       响应
     * @param userManagerDTO 条件查询参数
     */
    public void exportUserInfoExcel(HttpServletResponse response, UserManagerDTO userManagerDTO) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(PageDTO.DEFAULT_PAGE_NUM);
        pageDTO.setPageSize(PageDTO.MAX_PAGE_SIZE);
        PageVO<UserManagerVO> userPage = userService.getUserInfoPage(pageDTO, userManagerDTO);
        long total = userPage.getTotal();
        if (total == 0) {
            throw new LuckException("无用户数据导出");
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 总数大于PageDTO.MAX_PAGE_SIZE条
        if (total > PageDTO.MAX_PAGE_SIZE) {
            // 总共可以分多少页
            Integer pages = userPage.getPages();
            // 从第一页开始
            int page = 1;
            ExcelWriter excelWriter = null;
            try {
                // 初始化 ExcelWriter
                excelWriter = ExcelUtil.getExcelWriterMerge(response, UserExcelVO.EXCEL_NAME, UserExcelVO.MERGE_ROW_INDEX, UserExcelVO.MERGE_COLUMN_INDEX).build();
                // 大于 PageDTO.MAX_PAGE_SIZE行，进行分页查询
                // 计算有多少个sheet
                int sheetNum = countSlicesNum(SHEET_MAX_ROWS, total);
                for (int i = 0; i < sheetNum; i++) {
                    // 第i个sheet最多有多少行数据
                    long eachSheetRows = Math.min(total - i * SHEET_MAX_ROWS, SHEET_MAX_ROWS);
                    // 计算每个sheet的最大行数，要分多少次分页查询
                    int slicesNum = countSlicesNum(PageDTO.MAX_PAGE_SIZE.longValue(), eachSheetRows);
                    // 收集好获取到当前sheet的所有准备写入的用户数据
                    List<UserExcelVO> sheetDataList = new ArrayList<>();
                    for (int j = 0; j < slicesNum; j++) {
                        if (page > pages) {
                            break;
                        }
                        // 多线程分页查询数据
                        PageDTO pageIndex = new PageDTO();
                        pageIndex.setPageSize(PageDTO.MAX_PAGE_SIZE);
                        // 第几页开始
                        pageIndex.setPageNum(page);
                        int tempPage = page;
                        CompletableFuture<List<UserExcelVO>> userFuture = CompletableFuture.supplyAsync(() -> {
                            RequestContextHolder.setRequestAttributes(requestAttributes);
//                            List<UserManagerVO> userInfoList = userService.getUserInfoList(pageIndex, userManagerDTO);
                            List<UserManagerVO> userInfoList = userService.getUserInfoList(new PageAdapter(pageIndex), userManagerDTO);
                            List<UserExcelVO> list = mapperFacade.mapAsList(userInfoList, UserExcelVO.class);
                            // 给数组一个序列号
                            list.forEach(item -> item.setSeq(Integer.toString(list.indexOf(item) + (tempPage - 1) * pageIndex.getPageSize() + 1)));
                            return list;
                        }, userThreadPoolExecutor);
                        page++;
                        sheetDataList.addAll(userFuture.get());
                    }
                    // 导出excel
                    int sheetNo = i + 1;
                    WriteSheet writeSheet = EasyExcel.writerSheet(UserExcelVO.SHEET_NAME + sheetNo).head(UserExcelVO.class).build();
                    excelWriter.write(sheetDataList, writeSheet);
                }
            } catch (Exception e) {
                log.error("导出excel失败", e);
            } finally {
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        } else {
            // 小于等于 PageDTO.MAX_PAGE_SIZE行 直接导出
            List<UserExcelVO> list = mapperFacade.mapAsList(userPage.getList(), UserExcelVO.class);
            list.forEach(item -> item.setSeq(Integer.toString(list.indexOf(item) + 1)));
            ExcelUtil.soleExcel(response, list, UserExcelVO.EXCEL_NAME, UserExcelVO.MERGE_ROW_INDEX, UserExcelVO.MERGE_COLUMN_INDEX, UserExcelVO.class);
        }
    }

    /**
     * 下载用户导入模板
     *
     * @param response 响应
     */
    public void downloadModel(HttpServletResponse response) {
        List<UserExcelDTO> list = new ArrayList<>();
        list.add(new UserExcelDTO());
        Map<Integer, String[]> map = new HashMap<>(8);
        map.put(LEVEL_TYPE_INDEX, LEVEL_TYPE_MODE);
        map.put(SEX_INDEX, SEX_MODE);
        ExcelWriter excelWriter = null;
        try {
            // 先执行合并策略
            ExcelWriterBuilder excelWriterMerge = ExcelUtil.getExcelWriterMerge(response, UserExcelDTO.EXCEL_NAME, UserExcelDTO.MERGE_ROW_INDEX, UserExcelDTO.MERGE_COLUMN_INDEX);
            // useDefaultStyle false 不需要默认的头部加粗/自动换行样式
            excelWriter = ExcelUtil.getExcelModel(excelWriterMerge, map, 1).useDefaultStyle(false).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)) {
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet(UserExcelDTO.SHEET_NAME).head(UserExcelDTO.class).build();
                excelWriter.write(list, sheetWriter);
            }
        } catch (Exception e) {
            log.error("导出用户信息模板excel失败", e);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 分片个数，计算总数total分为每片为eachNum的片数
     *
     * @param eachNum 每片的个数
     * @param total   被分片的总数
     * @return 分片个数
     */
    private int countSlicesNum(Long eachNum, Long total) {
        boolean isZero = Objects.isNull(eachNum) || eachNum == 0 || Objects.isNull(total) || total == 0;
        if (isZero) {
            return 0;
        }
        // 分片数
        int pageSize = new BigDecimal(total).divide(new BigDecimal(eachNum), 1).intValue();
        // 余数
        int mod = new BigDecimal(total).divideAndRemainder(new BigDecimal(eachNum))[1].intValue();
        if (mod > 0) {
            pageSize = pageSize + 1;
        }
        return pageSize;
    }

    /**
     * 导入用户
     *
     * @param file
     * @return
     */
    public String importUserExcel(MultipartFile file) {
        try {
            Map<String, List<String>> errorMap = new HashMap<>(16);
            UserExcelListener userExcelListener = new UserExcelListener(this, errorMap);
            EasyExcel.read(file.getInputStream(), UserExcelDTO.class, userExcelListener).sheet().doRead();
            String info = getUserExportInfo(errorMap);
            return info;
        } catch (IOException e) {
            throw new LuckException(e.getMessage());
        }
    }

    /**
     * 处理导入的需要响应的信息
     *
     * @param errorMap 响应信息的集合
     * @return 响应信息
     */
    private String getUserExportInfo(Map<String, List<String>> errorMap) {
        StringBuffer info = new StringBuffer();
        List<String> importTotal = errorMap.get(UserExcelListener.IMPORT_ROWS);
        BigDecimal total = new BigDecimal("0");
        if (CollUtil.isNotEmpty(importTotal)) {
            for (int i = 0; i < importTotal.size(); i++) {
                String item = importTotal.get(i);
                if (StrUtil.isNotBlank(item)) {
                    total = total.add(new BigDecimal(item));
                }
            }
        }
        info.append("共有： " + total.intValue() + "条数据成功导入" + StrUtil.LF);
        // 错误信息
        List<String> errorRows = errorMap.get(UserExcelListener.ERROR_ROWS);
        if (CollUtil.isNotEmpty(errorRows)) {
            info.append("用户信息错误行数： " + errorRows.toArray() + StrUtil.LF);
        }
        List<String> errors = errorMap.get(UserExcelListener.OTHER);
        if (CollUtil.isNotEmpty(errors)) {
            for (String error : errors) {
                info.append(error);
            }
        }
        return info.toString();
    }


    /**
     * 根据导入的用户数据，批量注册用户
     *
     * @param list     导入的用户信息
     * @param errorMap 错误信息集合
     */
    public void importExcel(List<UserExcelDTO> list, Map<String, List<String>> errorMap) {
        if (CollUtil.isEmpty(list)) {
            throw new LuckException("解析到0条数据");
        }
        int size = list.size();
        // 全平台统一账户信息
        List<AuthAccountDTO> accountDTOList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        // 用户扩展表信息集合
        List<UserExtension> userExtensionList = new ArrayList<>();
        // 普通会员等级列表
        List<UserLevelVO> ordinaryLevels = userLevelService.list(LevelTypeEnum.ORDINARY_USER.value());
        // 付费会员等级列表
        List<UserLevelVO> payLevels = userLevelService.list(LevelTypeEnum.PAY_USER.value());
        // 集合去重复
        list = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(UserExcelDTO::getPhone))), ArrayList::new));
        // 第几行数据有误 的集合
        List<Integer> errorRows = new ArrayList<>();
        this.loadErrorData(list, errorMap, size);
        int row = 1;
        String ipAddr = IpHelper.getIpAddr();
        // 处理数据
        for (UserExcelDTO userExcelDTO : list) {
            row++;
            // 手机号必须是11位
            String phone = userExcelDTO.getPhone();
            if (StrUtil.isBlank(phone) || phone.length() != 11) {
                errorRows.add(row);
                continue;
            }
            // 密码不能为空
            if (StrUtil.isBlank(userExcelDTO.getPassword())) {
                errorRows.add(row);
                continue;
            }
            // 用户名不能为空
            if (StrUtil.isBlank(userExcelDTO.getUserName())) {
                errorRows.add(row);
                continue;
            }
            // 会员类型(0普通会员/1付费会员)，不填默认为普通会员
            Integer levelType = Objects.isNull(userExcelDTO.getLevelType()) ? LevelTypeEnum.ORDINARY_USER.value() : userExcelDTO.getLevelType();
            Date vipEndTime = userExcelDTO.getVipEndTime();
            // 如果是付费会员，会员结束时间没填写，则不能导入
            if (Objects.equals(LevelTypeEnum.PAY_USER.value(), levelType) && Objects.isNull(vipEndTime)) {
                errorRows.add(row);
                continue;
            }
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_USER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            Long userId = segmentIdResponse.getData();
            // 统一账户表信息
            AuthAccountDTO authAccountDTO = getInitAuthAccountDTO(ipAddr, userExcelDTO, userId);
            accountDTOList.add(authAccountDTO);
            // 成长值
            int growth = new BigDecimal(userExcelDTO.getGrowth()).intValue();
            // 会员等级
            Integer payUserLevel = null;
            if (Objects.equals(levelType, LevelTypeEnum.PAY_USER.value())) {
                if (CollUtil.isEmpty(payLevels)) {
                    throw new LuckException("请在平台端设置好付费会员的等级，才可以导入付费会员");
                }
                // 付费会员等级以导入为准，
                payUserLevel = Constant.USER_LEVEL_INIT;
                Integer importLevel = userExcelDTO.getLevel();
                for (UserLevelVO payLevel : payLevels) {
                    if (payLevel.getLevel() <= importLevel) {
                        payUserLevel = payLevel.getLevel();
                    }
                }
            }
            Integer normalLevel = getUserLevelByGrowth(ordinaryLevels, growth);
            User user = getInitUser(userExcelDTO, levelType, vipEndTime, userId, normalLevel, payUserLevel);
            userList.add(user);
            // 用户扩展表信息
            UserExtension userExtension = getInitUserExtension(userExcelDTO, growth, user);
            userExtensionList.add(userExtension);
        }
        // 批量注册用户
        batchRegisterUser(accountDTOList, userList, userExtensionList, errorMap);
        if (CollUtil.isNotEmpty(errorRows)) {
            List<String> collect = errorRows.stream().map(item -> item + "").collect(Collectors.toList());
            errorMap.get(UserExcelListener.ERROR_ROWS).addAll(collect);
        }
    }

    /**
     * 过滤用户手机号、用户名已经存在的用户
     * @param list 用户数据列表
     * @param size 数据数量
     */
    private void loadErrorData(List<UserExcelDTO> list, Map<String, List<String>> errorMap , int size) {
        List<String> errorPhones = new ArrayList<>();
        List<String> errorNames = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        for (UserExcelDTO userExcelDTO : list) {
            phones.add(userExcelDTO.getPhone());
            names.add(userExcelDTO.getUserName());
        }
        List<UserVO> userVOList = userService.getUserListByPhones(phones);
        phones = userVOList.stream().map(UserVO::getPhone).collect(Collectors.toList());

        ServerResponseEntity<List<String>> responseEntity = accountFeignClient.listUserNameByNames(names);
        if (responseEntity.isFail()) {
            throw new LuckException(responseEntity.getMsg());
        }
        List<String> data = responseEntity.getData();
        names = data;
        Iterator<UserExcelDTO> iterator = list.iterator();
        while (iterator.hasNext()) {
            UserExcelDTO userExcelDTO = iterator.next();
            boolean phone = phones.contains(userExcelDTO.getPhone());
            boolean name = names.contains(userExcelDTO.getUserName());
            if (!phone && !name) {
                phones.add(userExcelDTO.getPhone());
                names.add(userExcelDTO.getUserName());
                continue;
            }
            if (phone) {
                errorPhones.add(userExcelDTO.getPhone());
            } else {
                phones.add(userExcelDTO.getPhone());
            }
            if (name) {
                errorNames.add(userExcelDTO.getUserName());
            } else {
                names.add(userExcelDTO.getUserName());
            }
            iterator.remove();
        }
        List<String> errorList = errorMap.get(UserExcelListener.OTHER);
        if (CollUtil.isNotEmpty(errorPhones)) {
            errorList.add("手机号码：" + errorPhones.toString() + "已存在)");
        }
        if (CollUtil.isNotEmpty(errorNames)) {
            errorList.add("用户名：" + errorNames.toString() + "已存在)");
        }
    }

    /**
     * 批量注册用户
     *
     * @param accountDTOList    账户信息列表
     * @param userList          用户信息列表
     * @param userExtensionList 用户扩展信息列表
     * @param errorMap          错误信息集合
     */
    private void batchRegisterUser(List<AuthAccountDTO> accountDTOList, List<User> userList, List<UserExtension> userExtensionList, Map<String, List<String>> errorMap) {
        if (CollUtil.isEmpty(accountDTOList)) {
            return;
        }
        List<UserRegisterDTO> userRegisterList = mapperFacade.mapAsList(userList, UserRegisterDTO.class);
        List<UserRegisterExtensionDTO> userRegisterExtensionDTOList = mapperFacade.mapAsList(userExtensionList, UserRegisterExtensionDTO.class);
        // 只有先去全平台统一账户那里把账号注册成功了，才可以添加用户信息
        AuthAccountUserDTO authAccountUserDTO = new AuthAccountUserDTO();
        authAccountUserDTO.setAccountDTOList(accountDTOList);
        authAccountUserDTO.setUserRegisterList(userRegisterList);
        authAccountUserDTO.setUserRegisterExtensionDTOList(userRegisterExtensionDTOList);
        ServerResponseEntity<String> userRegisterResponse = accountFeignClient.batchRegisterAccount(authAccountUserDTO);
        if (!userRegisterResponse.isSuccess()) {
            throw new LuckException(userRegisterResponse.getMsg());
        }
        String row = userRegisterResponse.getData();
        errorMap.get(UserExcelListener.IMPORT_ROWS).add(row);
    }

    /**
     * 根据参数初始化用户扩展表信息
     *
     * @param userExcelDTO 导入参数
     * @param growth       成长值
     * @param user         用户信息
     * @return 用户扩展表信息
     */
    private UserExtension getInitUserExtension(UserExcelDTO userExcelDTO, int growth, User user) {
        UserExtension userExtension = new UserExtension();
        // 元转分
        BigDecimal price = new BigDecimal(userExcelDTO.getBalance());
        BigDecimal maxAmount = new BigDecimal(Long.valueOf(PriceUtil.MAX_AMOUNT).toString());
        if (price.compareTo(maxAmount) > 0) {
            price = maxAmount;
        }
        BigDecimal zero = new BigDecimal("0");
        if (zero.compareTo(price) > 0) {
            price = zero;
        }
        userExtension.setBalance(PriceUtil.toLongCent(price));
        userExtension.setActualBalance(userExtension.getBalance());
        userExtension.setGrowth(growth);
        userExtension.setLevel(user.getLevel());
        userExtension.setLevelType(user.getLevelType());
        userExtension.setScore(new BigDecimal(userExcelDTO.getScore()).longValue());
        userExtension.setVersion(0);
        userExtension.setSignDay(0);
        userExtension.setVipLevel(user.getVipLevel());
        userExtension.setUserId(user.getUserId());
        return userExtension;
    }

    /**
     * 根据参数初始化用户信息
     *
     * @param userExcelDTO 导入参数
     * @param levelType    会员类型
     * @param vipEndTime   vip结束时间
     * @param userId       用户id
     * @param normalLevel  普通会员等级
     * @param payUserLevel 付费会员等级
     * @return 用户信息
     */
    private User getInitUser(UserExcelDTO userExcelDTO, Integer levelType, Date vipEndTime, Long userId, Integer normalLevel, Integer payUserLevel) {
        // 用户信息
        User user = new User();
        user.setUserId(userId);
        user.setNickName(userExcelDTO.getNickName());
        user.setLevel(normalLevel);
        user.setVipLevel(payUserLevel);
        user.setStatus(UserStatusEnum.ENABLE.value());
        user.setPhone(userExcelDTO.getPhone());
        user.setSex(userExcelDTO.getSex());
        // 将日期强制格式化为yyyy-MM-dd
        String birthDate = dateStrFormatToStr(userExcelDTO.getBirthDate());
        user.setBirthDate(birthDate);
        user.setVipEndTime(vipEndTime);
        user.setLevelType(levelType);
        return user;
    }

    /**
     * 根据参数初始化 账户表
     *
     * @param ipAddr       ip
     * @param userExcelDTO 参数
     * @param userId       用户id
     * @return 账户信息
     */
    private AuthAccountDTO getInitAuthAccountDTO(String ipAddr, UserExcelDTO userExcelDTO, Long userId) {
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setCreateIp(ipAddr);
        authAccountDTO.setEmail(userExcelDTO.getEmail());
        authAccountDTO.setPassword(passwordEncoder.encode(userExcelDTO.getPassword()));
        authAccountDTO.setPhone(userExcelDTO.getPhone());
        // 普通用户，不是管理员
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        String userName = userExcelDTO.getUserName();
        if (StrUtil.isBlank(userName)) {
            userName = TEL_PREFIX + authAccountDTO.getPhone();
        }
        authAccountDTO.setUsername(userName);
        authAccountDTO.setStatus(UserStatusEnum.ENABLE.value());
        authAccountDTO.setUserId(userId);
        return authAccountDTO;
    }


    /**
     * 根据成长值获取成长值所在等级
     *
     * @param levelVOList 会员等级列表
     * @param growth      成长值
     * @return 会员等级
     */
    private Integer getUserLevelByGrowth(List<UserLevelVO> levelVOList, Integer growth) {
        int level = Constant.USER_LEVEL_INIT;
        for (UserLevelVO levelVO : levelVOList) {
            if (growth >= levelVO.getNeedGrowth()) {
                level = levelVO.getLevel();
            }
        }
        return level;
    }

    /**
     * 将日期格式为 yyyy-MM-dd
     *
     * @param dateStr 日期字符串
     * @return yyyy-MM-dd日期字符串
     */
    private String dateStrFormatToStr(String dateStr) {
        if (StrUtil.isNotBlank(dateStr) && dateStr.length() > YEAR_MONTH_DAY_LENGTH) {
            try {
                DateTime parse = DateUtil.parse(dateStr, UserExcelManager.FORMAT);
                dateStr = DateUtil.format(parse, UserExcelManager.FORMAT);
            } catch (Exception e) {
                log.info("导入时，用户出生日期格式化异常");
                dateStr = "";
            }
        }
        return dateStr;
    }
}
