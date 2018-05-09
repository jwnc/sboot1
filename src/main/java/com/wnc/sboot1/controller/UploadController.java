
package com.wnc.sboot1.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.common.beans.ResultBean;
import com.wnc.sboot1.itbook.MyAppParams;
import com.wnc.sboot1.itbook.helper.BookLogRetrieving;
import com.wnc.sboot1.itbook.service.ITBookLogService;

@RestController
@RequestMapping( value = "/upload" )
public class UploadController
{
    final static Logger logger = Logger.getLogger( UploadController.class );

    @RequestMapping( "/lastTime" )
    public ResultBean<Map> lastTime( @RequestParam( "device" ) String device )
    {
        logger.info( "Query Upload LastTime:" + device );
        return new ResultBean<Map>( BookLogRetrieving.getLastTime( device ) );
    }

    @PostMapping( "/txt" )
    public ResultBean<String> handelFileUpload(
            @RequestParam( "file" ) MultipartFile file,
            @RequestParam( value = "client", defaultValue = "PC" ) String client,
            RedirectAttributes redirectAttributes, HttpServletRequest request )
            throws IllegalStateException, IOException
    {
        String retData = "上传失败!";
        if ( !validClient( client ) )
        {
            retData = "非法的客户端";
        } else
        {
            String fileName = "";
            if ( file != null )
            {
                fileName = file.getOriginalFilename();// 文件原名称
                logger.info( "上传的文件原名称:" + fileName );
                String type = fileName.indexOf( "." ) != -1
                        ? fileName.substring( fileName.lastIndexOf( "." ) + 1,
                                fileName.length() )
                        : null;
                if ( type != null )
                {
                    if ( "TXT".equals( type.toUpperCase() ) )
                    {
                        String folder = getFolder( request, client );

                        String path = BasicFileUtil.getMakeFilePath( folder,
                                fileName );
                        logger.info( "存放文件的路径:" + path );
                        file.transferTo( new File( path ) );

                        BookLogRetrieving.log( client, fileName,
                                BasicDateUtil.getCurrentDateTimeString() );
                        ITBookLogService.parseLogsToDb( path, client );

                        retData = "上传文件【" + fileName + "】成功!";
                    } else
                    {
                        retData = "不是我们想要的文件类型,请按要求重新上传";
                    }
                } else
                {
                    retData = "上传文件类型为空";
                }
            } else
            {
                retData = "没有找到相对应的文件";
            }
        }
        logger.info( retData );
        return new ResultBean<String>( retData );
    }

    private boolean validClient( String client )
    {
        return StringUtils.isNotBlank( client );
    }

    private String getFolder( HttpServletRequest request, String client )
    {
        // 项目在容器中实际发布运行的根路径
        // String realPath = request.getSession().getServletContext()
        // .getRealPath( "/" );

        String deviceName = client.equalsIgnoreCase( "PC" )
                ? System.getenv().get( "COMPUTERNAME" )
                : client;

        String folder = BasicFileUtil.getMakeFilePath(
                MyAppParams.getInstance().getitwordSaveFolder(), deviceName );
        BasicFileUtil.makeDirectory( folder );
        return folder;
    }
}