package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.biz.config.DomainConfig;
import com.mall4j.cloud.biz.controller.MinioUploadController;
import com.mall4j.cloud.biz.dto.cp.CpMaterialMsgImgSelectDTO;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialMsgImgMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialMsgImg;
import com.mall4j.cloud.biz.service.cp.CpMaterialMsgImgService;
import com.mall4j.cloud.biz.vo.cp.CpMaterialMsgImgVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.word.pdf.PdfToImageUtil;
import com.word.ppt.PPTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-12-06 16:14:28
 */
@Slf4j
@Service
@RefreshScope
public class CpMaterialMsgImgServiceImpl extends ServiceImpl<CpMaterialMsgImgMapper,CpMaterialMsgImg> implements CpMaterialMsgImgService {

//    @Value("${biz.oss.resources-url}")
//    private String resources_url;
    @Autowired
    private DomainConfig domainConfig;
    @Autowired
    private CpMaterialMsgImgMapper cpMaterialMsgImgMapper;
    @Autowired
    private MinioUploadController minioUploadController;



    @Override
    public List<CpMaterialMsgImgVO> filePreview(CpMaterialMsgImgSelectDTO dto) {
        return cpMaterialMsgImgMapper.mobileList(dto);
    }

    @Override
    public void formtFileAndSaveTo(Long matId, Long matMsgId, String path,String fileName) {
        log.info("素材文件转图片--> matId:{} matMsgId:{} path:{} fileName:{} ",matId,matMsgId,path,fileName);
        if(!checkFileType(fileName)){
            log.info("素材文件转图片失败，非【pdf/ppt】文件");
            return;
        }
        ThreadUtil.execute(()->{
            try {
                long start = new Date().getTime();
                String downloadUrl=domainConfig.getDomain()+path;
                log.info("素材文件转图片,需要下载文件路径: {}",downloadUrl);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String suffix = FileUtil.getSuffix(fileName);
                File file = File.createTempFile("tmpfile_"+dateFormat.format(new Date()),"."+suffix);
                File localFile=HttpUtil.downloadFileFromUrl(downloadUrl,file);
                log.info("素材文件转图片,已下载下载文件路径: {}",localFile.getAbsolutePath());
                File pdf = new File("/tmp/tmpWord_"+dateFormat.format(new Date())+".pdf");
                List<String> images=new ArrayList<>();//文件转图片路径集合
                if(isPdf(fileName)){//pdf
                    images=convertPDFToImages(localFile);
                }else if(isPPT(fileName)){//ppt
                    images=convertPPTToImages(localFile);
                }else if(isWord(fileName)){//word
                    String doc = file.getAbsolutePath();
                    FileOutputStream os = new FileOutputStream(pdf);
                    Document d = new Document(doc);
                    d.save(os, SaveFormat.PDF);
                    os.close();
                    images = convertPDFToImages(pdf);
                }
                if(CollUtil.isEmpty(images)){
                    log.info("素材文件转图片保存失败1，图片数据为空");
                    return;
                }
                List<String> imageUrls=new ArrayList<>();//本地文件上传结果集合
                String contentType = ContentType.APPLICATION_OCTET_STREAM.toString();
                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                for (int i = 0; i < images.size(); i++) {
                    File imgFile = new File(images.get(i));
                    MultipartFile multipartFile = new MultipartFileDto(imgFile.getName(), imgFile.getName(), contentType, new FileInputStream(imgFile));
                    String mimoPath = "material/file/img/" + time + "/" + i+"_"+multipartFile.getOriginalFilename();
                    log.info("mimoPath---> :{}",mimoPath);
                    ServerResponseEntity<String> responseEntity = minioUploadController.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());//上传oss
                    log.info("上传文件地址：{}" , JSON.toJSONString(responseEntity.getData()));
                    if (responseEntity.isSuccess()) {
                        imageUrls.add(responseEntity.getData());
                        cn.hutool.core.io.FileUtil.del(imgFile);//删除临时文件文件
                    }
                }
                if(CollUtil.isEmpty(imageUrls)){
                    log.info("素材文件转图片保存失败2，图片数据为空");
                    return;
                }
                //保存入库
                List<CpMaterialMsgImg> materialMsgImgs=new ArrayList<>();
                for (int i = 0; i < imageUrls.size(); i++) {
                    CpMaterialMsgImg materialMsgImg=new CpMaterialMsgImg();
                    materialMsgImg.setMatId(matId);
                    materialMsgImg.setMatMsgId(matMsgId);
                    materialMsgImg.setImage(imageUrls.get(i));
                    materialMsgImg.setIsDelete(0);
                    materialMsgImg.setCreateTime(new Date());
                    materialMsgImg.setCreateBy(AuthUserContext.get().getUsername());
                    materialMsgImg.setSeq(i);
                    materialMsgImgs.add(materialMsgImg);
                }
                //先删除之前数据
                cpMaterialMsgImgMapper.deleteByMatId(matId);
//                cpMaterialMsgImgMapper.deleteByMatMsgId(matMsgId);

                this.saveBatch(materialMsgImgs);
                cn.hutool.core.io.FileUtil.del(localFile);//删除临时文件文件
                cn.hutool.core.io.FileUtil.del(pdf);//删除word转换后的pdf文件

                long end = new Date().getTime();
                log.info("素材文件转图片成功，耗时：{}s", ((end - start) / 1000.0) );
            }catch (Exception e){
                log.info("素材文件转图片失败: {}",e);
            }
        });

    }


    public List<String> convertPDFToImages(File localFile) {
        return PdfToImageUtil.pdf2imgs(localFile.getAbsolutePath(),"");
    }

    public List<String> convertPPTToImages(File localFile) {
        try {
            log.info("开始执行ppt转pdf,pdf转图片 文件：{}",localFile.getAbsolutePath());
            long start = new Date().getTime();
            List<String> images=new ArrayList<>();
            PPTUtils.ppt2Imgs(localFile,"",5,images);
            long end = new Date().getTime();
            cn.hutool.core.io.FileUtil.del(localFile);//删除ppt临时文件
            log.info("ppt-to-images，耗时：{}s", ((end - start) / 1000.0) );
            return images;
        } catch (Exception e) {
            log.info("convertPPTToImages error : {}",e);
            throw new LuckException("操作失败");
        }
    }

    public boolean checkFileType(String fileName){
        String prefix = FileUtil.getSuffix(fileName);
        if(prefix.equalsIgnoreCase("pdf")
                || prefix.equalsIgnoreCase("ppt")
                || prefix.equalsIgnoreCase("pptx")
                || prefix.equalsIgnoreCase("doc")
                || prefix.equalsIgnoreCase("docx")){
            return true;
        }
        return false;
    }

    public boolean isPdf(String fileName){
        String prefix = FileUtil.getSuffix(fileName);
        if(prefix.equalsIgnoreCase("pdf")){
            return true;
        }
        return false;
    }

    public boolean isPPT(String fileName){
        String prefix = FileUtil.getSuffix(fileName);
        if(prefix.equalsIgnoreCase("ppt")
                || prefix.equalsIgnoreCase("pptx")){
            return true;
        }
        return false;
    }

    public boolean isWord(String fileName){
        String prefix = FileUtil.getSuffix(fileName);
        if(prefix.equalsIgnoreCase("doc") || prefix.equalsIgnoreCase("docx")){
            return true;
        }
        return false;
    }

    public static void main(String[] strings){
        String name="353b97769698466dacd7fa7d3f922e1d.pdf";
        String extension = name.substring(name.lastIndexOf(".") + 1);
        System.out.println(FileUtil.getName(name));
        System.out.println(extension);
        String doc = "E:\\桌面\\work\\新建文档.doc";
        File pdf = new File("E:\\桌面\\work\\佣金提现规则.pdf");
        try{
            FileOutputStream os = new FileOutputStream(pdf);
            Document d = new Document(doc);
            d.save(os,SaveFormat.PDF);
            os.close();
        }catch (Exception e){

        }
    }
}
