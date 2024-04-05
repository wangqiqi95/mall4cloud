package com.mall4j.cloud.biz.controller.app.staff;

import com.mall4j.cloud.api.biz.dto.cp.chat.SessionFileDTO;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.api.biz.vo.StaffSessionVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.service.chat.SessionFileService;
import com.mall4j.cloud.biz.service.chat.SessionSearchService;
import com.mall4j.cloud.biz.wx.wx.util.LangUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import javax.imageio.stream.FileImageOutputStream;
import javax.validation.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@RestController("staffSessionFileController")
@RequestMapping("/s/staffSession")
@Api(tags = "移动端会话存档记录")
public class StaffSessionFileController {

    @Autowired
    SessionSearchService sessionSearchService;
    @Autowired
    SessionFileService sessionFileService;
    @Autowired
    private MapperFacade mapperFacade;
    @GetMapping("/list")
    @ApiOperation(value = "获取会话存档列表", notes = "分页获取会话存档列表")
    public ServerResponseEntity<PageVO<SessionFileVO>> page(@Valid PageDTO pageDTO, SessionFileDTO fileDTO) {
        PageVO<SessionFileVO> filePage = sessionSearchService.getSessionFile(pageDTO, fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("getStaffCount")
    @ApiOperation(value = "查询员工会话总量", notes = "查询员工会话总量")
    public ServerResponseEntity<StaffSessionVO> getStaffCount(SessionFileDTO fileDTO) {
        StaffSessionVO filePage = sessionSearchService.getStaffCount(fileDTO);
        return ServerResponseEntity.success(filePage);
    }

    @GetMapping("getUserAndStaffCount")
    @ApiOperation(value = "查询员工会话总量", notes = "查询员工会话总量")
    public ServerResponseEntity<List<StaffSessionVO>> getUserAndStaffCount(SessionFileDTO fileDTO) {
        List<StaffSessionVO> filePage = sessionSearchService.getUserAndStaffCount(fileDTO);
        return ServerResponseEntity.success(filePage);
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

    @PostMapping("/getSessionFileByMsgId")
    @ApiOperation(value = "根据msgId查询消息", notes = "根据msgId查询消息")
    public ServerResponseEntity<List<SessionFileVO>> getSessionFileByMsgId(@RequestBody SessionFileDTO fileDTO) {
        List<SessionFileVO> files = sessionSearchService.getSessionFileByMsgId(fileDTO.getMsgIds());
        return ServerResponseEntity.success(files);
    }

    @GetMapping("getAuditVoiceFile")
    @ApiOperation(value = "获取语音", notes = "获取语音")
    public ServerResponseEntity<byte[]> getAuditVoiceFile(@RequestParam("msgId") String msgId){
        byte[] bys = null;
        try {
            InputStream inputStream = sessionFileService.getFile(msgId);
            if (null != inputStream) {
                byte[] bytes = LangUtil.getBytes(inputStream);
                File so = new File("/data/var/log/"+msgId+".amr");
                FileImageOutputStream out = new FileImageOutputStream(so);
                out.write(bytes,0,bytes.length);
                out.close();;
                inputStream.close();
                File ta = new File("/data/var/log/"+msgId+".mp3");
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
}
