package com.smartwf.common.webservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.json.JSONObject;
import org.json.XML;

import com.smartwf.common.pojo.WsResult;

/**
 * Httpclient
 */
public class HttpclientCall {


    /**
     * Httpclient调用webservice
     * @param url
     * @param header
     * @param patameterMap
     * @return WsResult
     * @throws IOException
     */
    public static WsResult invoke(String url, Map<String, Object> header, Map<String, Object> patameterMap) throws IOException {
        // 构造xml
        StringBuffer soapRequestData = new StringBuffer();
        soapRequestData.append("<?xml version='1.0' encoding='utf-8'?>");
        soapRequestData.append("<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'"
                        + " xmlns:xsd='http://www.w3.org/2001/XMLSchema'"
                        + " xmlns:soap='http://www.w3.org/2003/05/soap-envelope'>");
        // 设置Header
        soapRequestData.append("<soap:Header>")
                .append("<Identify xmlns='FjUpdate1'>")
                .append("<UserName>").append(header.get("UserName")).append("</UserName>")
                .append("<Password>").append(header.get("Password")).append("</Password>")
                .append("<FileName>").append(header.get("FileName")).append("</FileName>")
                .append("<FileSize>").append(header.get("FileSize")).append("</FileSize>")
                .append("<FileCreateTime>").append(header.get("FileCreateTime")).append("</FileCreateTime>")
                .append("</Identify>")
                .append("</soap:Header>");

        soapRequestData.append("<soap:Body>");
        soapRequestData.append("<UpFile xmlns='FjUpdate1'>");
        // 设置参数
        Set<String> nameSet = patameterMap.keySet();
        for (String name : nameSet) {
            soapRequestData.append("<" + name + ">" + patameterMap.get(name) + "</" + name + ">");
        }

        soapRequestData.append("</UpFile>");
        soapRequestData.append("</soap:Body>");
        soapRequestData.append("</soap:Envelope>");
        // 调用
        PostMethod postMethod = new PostMethod(url);

        byte[] bytes = soapRequestData.toString().getBytes("utf-8");
        InputStream inputStream = new ByteArrayInputStream(bytes, 0, bytes.length);
        RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, bytes.length, "application/soap+xml; charset=utf-8");
        postMethod.setRequestEntity(requestEntity);

        HttpClient httpClient = new HttpClient();
        // 调用状态码
        int statusCode = httpClient.executeMethod(postMethod);
        // 调用信息
        String soapResponseData = postMethod.getResponseBodyAsString();
        JSONObject jsonObject = XML.toJSONObject(soapResponseData);
        JSONObject jsonObject1 = jsonObject.getJSONObject("soap:Envelope");
        JSONObject jsonObject2 = jsonObject1.getJSONObject("soap:Body");
        JSONObject upFileResponse = jsonObject2.getJSONObject("UpFileResponse");
        boolean data = upFileResponse.getBoolean("UpFileResult");
        String msg = upFileResponse.getString("pErrMsg");
        return new WsResult(statusCode, data, msg);
    }

}