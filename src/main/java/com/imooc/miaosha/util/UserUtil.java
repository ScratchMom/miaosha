package com.imooc.miaosha.util;

/**
 * @author langwang
 * @date 2018/8/30 下午8:46
 * @Description: TODO
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.miaosha.domain.MiaoshaUser;

public class UserUtil {

    private static void createUser(int count) throws Exception{
        List<MiaoshaUser> users = new ArrayList<MiaoshaUser>(count);
        long addListStart = System.currentTimeMillis();
        //生成用户
        for(int i=0;i<count;i++) {
            MiaoshaUser user = new MiaoshaUser();
            user.setId(13000000000L+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            users.add(user);
        }
        long addListEnd = System.currentTimeMillis();
        long addList = addListEnd - addListStart;
        System.out.println("插入list 时间 === " + addList + "毫秒");
        System.out.println("create user");
//        //插入数据库
//        Connection conn = DBUtil.getConn();
//        String sql = "insert into miaosha_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        long batchSqlStart = System.currentTimeMillis();
//        for(int i=0;i<users.size();i++) {
//            MiaoshaUser user = users.get(i);
//            pstmt.setInt(1, user.getLoginCount());
//            pstmt.setString(2, user.getNickname());
//            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
//            pstmt.setString(4, user.getSalt());
//            pstmt.setString(5, user.getPassword());
//            pstmt.setLong(6, user.getId());
//            pstmt.addBatch();
//        }
//        pstmt.executeBatch();
//        pstmt.close();
//        conn.close();
//        long batchSqlEnd = System.currentTimeMillis();
//        long batchSql = batchSqlEnd - batchSqlStart;
//        System.out.println("插入sql 时间 === " + batchSql + "毫秒");
//        System.out.println("insert to db");
        //登录，生成token
        String urlString = "http://localhost:8013/api/login/do_login";
        File file = new File("/Users/meng/Desktop/tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            MiaoshaUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            String params = "mobile="+user.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());

            String row = user.getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args)throws Exception {
        createUser(5000);
    }
}
