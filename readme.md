client->server:
1、Require请求类:（需要服务端进行处理）
   数据成员：String reqtype:客户端表示是登录、注册、退出
            String username：
            String password：
   （1）请求登录/注册：
   （2）请求退出：
2、Message转发类:(服务端直接进行转发)（服务端会执行操作：发给个人或发给全部人；但不修改里面的内容）
   数据成员：String userfrom:
            String userto:（群聊的话就“*”，私聊则某人的username）
            String words:
（1）正常消息：
（2）匿名消息：(客户端进行匿名处理)
（3）私聊消息：

server->client
1、response回应类：（需要服务端进行处理）
   数据成员：String restype:服务端表示是登录、注册、退出（回应退出是为了终止客户端的receive thread）
               String username：
               boolean state：表示注册成功或失败,（虽然还没想过，但也可能表示退出失败）
               String[] userlist:
   （1）回应登录：
      登录/注册成功：
      登录/注册失败：【这个是不会发给其他人】
   （2）回应退出：
2、Message转发类：（服务端直接进行转发）
（1）
（2）
（3）


User类：
username:
password:


client->server:
type:Require/Response/ServerMessage/ClientMessage
    Require:
        1、String reqtype
        2、User user
    Response:
        1、String reqtype
        2、String username
        3、boolean state
        4、Arraylist<String> usernamelist
    ServerMessage:
        1、String way:"All"/"Some"/"One"；
        2、Arraylist<String> fctype:(function type)匿名；
        3、String texttype:(txt/png/face/words);
        4、String userfrom:
        5、Arraylist<String> userto:
        6、String words:（有/无）
     ***   6、* data:(png:byte[];txt:String;face:String)
    ClientMessage:
        1、String way:"All"/"Some"/"One";
        3、String texttype：
        4、String userfrom:
        6、String words:（有/无）
     ***    6、* data: 

Package:
    indexlist
    data
    mode
    datahead(有/无)


1、Require请求类:（需要服务端进行处理）
   数据成员：String reqtype:客户端表示是登录、注册、退出
            User user;
   （1）请求登录/注册：
   （2）请求退出：
2、Message转发类:(服务端直接进行转发)（服务端会执行操作：发给个人或发给全部人；但不修改里面的内容）
   数据成员：String userfrom:
            String userto:（群聊的话就“*”，私聊则某人的username）
            String words:
（1）正常消息：
（2）匿名消息：(客户端进行匿名处理)
（3）私聊消息：

server->client
1、response回应类：（需要服务端进行处理）
   数据成员：String restype:服务端表示是登录、注册、退出（回应退出是为了终止客户端的receive thread）
               String username：
               boolean state：表示注册成功或失败,（虽然还没想过，但也可能表示退出失败）
               String[] userlist:
   （1）回应登录：
      登录/注册成功：
      登录/注册失败：【这个是不会发给其他人】
   （2）回应退出：
2、Message转发类：（服务端直接进行转发）
（1）
（2）
（3）