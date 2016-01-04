package com.example.testnetty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;



import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class CommandSocket {
	private static CommandSocket instance;//我只是测试用
	private String ip = "192.168.42.1";// socket鍛戒护IP
	private int port = 7878;// socket鍛戒护绔彛
	public ChannelFuture future = null;
	String TAG = "CommandSocket";
	Bootstrap bootstrap;
	java.util.Timer HeartTimer;
	 Channel channel;
	public static CommandSocket getInstance() {
		if (instance == null)
			instance = new CommandSocket();
		return instance;

	}

	public void postCommand(String StrCommand) {
		Log.d(TAG, StrCommand);
		if (future != null && future.channel() != null) {
			future.channel().write(StrCommand);
		} else {
			instance = new CommandSocket();
		}
	}

	public void connect() {
		 channel.write("{\"msg_id\":257,\"token\":0}");
//		if (null != future && null != future.channel() && future.channel().isOpen()
//				&& future.channel().isActive()) {
//			Log.d(getClass().getCanonicalName(), "future channel is already open");
//			
//			future.channel().close();
//			
//		}
//
//
//		new Thread() {
//			public void run() {
//				// 鍒涘缓鏃犺繛鎺ヤ紶杈揷hannel鐨勮緟鍔╃被(UDP),鍖呮嫭client鍜宻erver
//				try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//				future = bootstrap.connect(new InetSocketAddress(ip, port));
//
//			}
//		}.start();

	}


	public CommandSocket() {
		bootstrap = new Bootstrap();
	
		
		 EventLoopGroup group = new NioEventLoopGroup();
         try {
             Bootstrap bootstrap  = new Bootstrap()
                     .group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new CommandChannelHandler());
              channel = bootstrap.connect(ip, port).sync().channel();
             BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
             while(true){
                 //channel.writeAndFlush(in.readLine() + "\r\n");
                 channel.writeAndFlush("{\"msg_id\":257,\"token\":0}");
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             group.shutdownGracefully();
         }

	}





	class CommandChannelHandler extends SimpleChannelInboundHandler<String>  {

		@Override
		public void connect(ChannelHandlerContext ctx,
				SocketAddress remoteAddress, SocketAddress localAddress,
				ChannelPromise promise) throws Exception {
			// TODO Auto-generated method stub
			Log.d(getClass().getCanonicalName(), "CommandChannelHandler connect");
			super.connect(ctx, remoteAddress, localAddress, promise);
		}

		@Override
		public void close(ChannelHandlerContext ctx, ChannelPromise promise)
				throws Exception {
			// TODO Auto-generated method stub
			if (future.channel().isOpen() && future.channel().isActive()) {
				Log.d(getClass().getCanonicalName(), "CommandChannelHandler close");
				
				future.channel().close();
			}
			super.close(ctx, promise);
		}

		@Override
		protected void messageReceived(ChannelHandlerContext arg0, String arg1)
				throws Exception {
			// TODO Auto-generated method stub
			Log.d(getClass().getCanonicalName(),
					"--------messageReceived-------");
		}
	}

}
