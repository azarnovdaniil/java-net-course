package ru.daniilazarnov.handlers.in;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.datamodel.RequestData;
import ru.daniilazarnov.datamodel.ResponseData;
import ru.daniilazarnov.db.AuthenticationService;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        //test
        ResponseData rd = new ResponseData();
        rd.setIntValue(555);
        ctx.write(rd);


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //Авторизация
        // Если авторизация успешна, то отправляем структуру хранилища пользователя
        // иначе сообщение и ctx.close();
        AuthenticationService as = new AuthenticationService();
        getAuthorisationProcess(as);


        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        responseData.setIntValue(requestData.getIntValue() * 2);// tested response
        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
        System.out.println(requestData);
    }

    private void getAuthorisationProcess(AuthenticationService as) {
    }

}
