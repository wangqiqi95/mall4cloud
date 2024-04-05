package com.mall4j.cloud.biz.controller.wx.chat;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.service.chat.HistorySessionService;
import com.mall4j.cloud.biz.service.chat.SessionFileService;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.wx.wx.util.LangUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import javax.imageio.stream.FileImageOutputStream;
import javax.validation.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@RestController("sessionFileController")
@RequestMapping("/p/session_file")
@Api(tags = "会话存档记录")
public class SessionFileController {

    @Autowired
    private SessionFileService sessionFileService;
    @Autowired
    private SessionSearchService sessionSearchService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private HistorySessionService historySessionService;
    @GetMapping("/list")
    @ApiOperation(value = "获取会话存档列表", notes = "分页获取会话存档列表")
    public ServerResponseEntity<PageVO<SessionFileVO>> page(@Valid PageDTO pageDTO, SessionFileDTO fileDTO) {
        SessionFileVO sessionFile = mapperFacade.map(fileDTO, SessionFileVO.class);
        PageVO<SessionFileVO> filePage = sessionFileService.page(pageDTO, fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @PostMapping("save")
    @ApiOperation(value = "保存会话存档", notes = "保存会话存档")
    public ServerResponseEntity<Void> save(@RequestBody SessionFileDTO fileDTO){
        SessionFile sessionFile = mapperFacade.map(fileDTO, SessionFile.class);
        sessionFileService.save(sessionFile);
        return ServerResponseEntity.success();
    }

    @GetMapping("single_agree")
    @ApiOperation(value = "获取同意情况", notes = "获取同意情况")
    public ServerResponseEntity<Void> singleAgree(){
        sessionFileService.singleAgree();
        return ServerResponseEntity.success();
    }

    @GetMapping("getEmotionFile")
    @ApiOperation(value = "获取表情包", notes = "获取表情包")
    public ServerResponseEntity<String> getEmotionFile(@RequestParam("msgId") String msgId){
        SessionFile sessionFile=sessionSearchService.getSessionFile(msgId);
        if(Objects.nonNull(sessionFile)){
            return ServerResponseEntity.success(sessionFile.getStreamBaseData());
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("getAuditImageFile")
    @ApiOperation(value = "获取图片", notes = "获取图片")
    public ServerResponseEntity<byte[]> getAuditFile(@RequestParam("msgId") String msgId){
        try {
            InputStream inputStream = sessionFileService.getFile(msgId);
            if (null != inputStream) {
                byte[] bytes = LangUtil.getBytes(inputStream);
                FileImageOutputStream out = new FileImageOutputStream(new File("D:\\temp\\test.jpg"));
                out.write(bytes,0,bytes.length);
                out.close();;
                inputStream.close();
                //String base = Base64.getEncoder().encodeToString(bytes);
                //System.out.println(base);
                return ServerResponseEntity.success(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("getAuditVoiceFile")
    @ApiOperation(value = "获取图片", notes = "获取图片")
    public ServerResponseEntity<byte[]> getAuditVoiceFile(@RequestParam("msgId") String msgId){
        byte[] bys = null;
        try {
            InputStream inputStream = sessionFileService.getFile(msgId);
            if (null != inputStream) {
                byte[] bytes = LangUtil.getBytes(inputStream);
                /*String contentType = ContentType.APPLICATION_OCTET_STREAM.toString();
                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                MultipartFile multipartFile = new MultipartFileDto("ceshi.amr", "ceshi.amr", contentType, inputStream);
                String mimoPath = "material/file/img/" + time + "_"+multipartFile.getOriginalFilename();
                ServerResponseEntity<String> responseEntity = minioUploadController.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());//上传oss
                String fileUrl = "";
                if (responseEntity.isSuccess()) {
                    fileUrl = responseEntity.getData();
                    cn.hutool.core.io.FileUtil.del(imgFile);//删除临时文件文件
                }*/
                File so = new File("/data/var/log/"+msgId+".amr");
                /*if(so.exists()){
                    so.delete();
                }else{
                    so.createNewFile();
                }*/
                FileImageOutputStream out = new FileImageOutputStream(so);
                out.write(bytes,0,bytes.length);
                out.close();;
                inputStream.close();
                //File so = new File("jniLibs/ceshi.amr");
                File ta = new File("/data/var/log/"+msgId+".mp3");
                /*if(ta.exists()){
                    ta.delete();
                }else{
                    ta.createNewFile();
                }*/
                AudioAttributes audio = new AudioAttributes();
                Encoder en = new Encoder();
                audio.setCodec("libmp3lame");
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("mp3");
                attrs.setAudioAttributes(audio);
                try{
                    en.encode(new MultimediaObject(so),ta,attrs);
                    bys = FileUtils.readFileToByteArray(ta);
                    String base = java.util.Base64.getEncoder().encodeToString(bys);
                    cn.hutool.core.io.FileUtil.del("/data/var/log/"+msgId+".amr");//删除临时文件文件
                    cn.hutool.core.io.FileUtil.del("/data/var/log/"+msgId+".mp3");//删除临时文件文件
                    return ServerResponseEntity.success(bys);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponseEntity.success(bys);
    }

    @GetMapping("delete")
    @ApiOperation(value = "删除会话存档", notes = "删除会话存档")
    public ServerResponseEntity<Void> delete(@RequestParam("msgId") String msgId){
        sessionFileService.delete(msgId);
        return ServerResponseEntity.success();
    }

    @ApiOperation(value = "导入会话存档记录")
    @PostMapping("/importSessionExcel")
    public ServerResponseEntity<String> importSessionExcel(@RequestPart("excelFile") MultipartFile file) {
        if (file == null) {
            throw new LuckException("网络繁忙，请稍后重试");
        }
        historySessionService.importExcel(file);
        return  ServerResponseEntity.success();
    }
}
