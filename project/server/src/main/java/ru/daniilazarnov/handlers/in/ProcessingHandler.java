package ru.daniilazarnov.handlers.in;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.datamodel.RequestData;
import ru.daniilazarnov.datamodel.ResponseData;
import ru.daniilazarnov.db.AuthenticationService;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    private AuthenticationService as;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        as = new AuthenticationService();
        //test
        int t = 555;
        ResponseData rd = new ResponseData();
        rd.setIntValue(t);
        ctx.write(rd);


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //Авторизация
        // Если авторизация успешна, то отправляем структуру хранилища пользователя
        // иначе сообщение и ctx.close();

        getAuthorisationProcess(as);


        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        responseData.setIntValue(requestData.getId() * 2); //tested response
        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
        System.out.println(requestData);
    }

    private void getAuthorisationProcess(AuthenticationService as) {
//        sendMessage("Please enter credentials. Sample [-auth login password]");
//        try {
//            /**
//             * -auth login password
//             * sample: -auth l1 p1
//             */
//            while (true) {
//                String mayBeCredentials = in.readUTF();
//                if (mayBeCredentials.startsWith("-auth")) {
//                    logger.info(String.format("[%s] sent -auth command", name));
//                    String[] credentials = mayBeCredentials.split("\\s");
//                    String mayBeNickname = chat.getAuthenticationService()
//                            .findNicknameByLoginAndPassword(credentials[1], credentials[2]);
//                    if (mayBeNickname != null) {
//                        if (!chat.isNicknameOccupied(mayBeNickname)) {
//                            sendMessage("[INFO] Auth OK");
//                            logger.info("Auth OK");
//                            name = mayBeNickname;
//
//                            chat.broadcastMessage(String.format("[%s] logged in", name));
//                            logger.info(String.format("[%s] logged in", name));
//                            chat.subscribe(this);
//
//                            checkPeriod = true;
//
//                            return;
//                        } else {
//                            sendMessage("[INFO] Current user is already logged in.");
//                            logger.info("Current user is already logged in.");
//                        }
//                    } else {
//                        sendMessage("[INFO] Wrong login or password.");
//                        logger.info("Wrong login or password.");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e);
//            throw new RuntimeException("SWW", e);
//        }
    }

}
