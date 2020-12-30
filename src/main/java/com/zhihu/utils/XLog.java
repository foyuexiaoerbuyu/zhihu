package com.zhihu.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import javax.swing.filechooser.FileSystemView;

public class XLog {

    /**
     * log输出到本地文件开关
     */
    private static boolean offset = false;
    private static int stepNumber = 0;

    public static void showLogArgs(Object... args) {
        System.out.println(new Throwable().getStackTrace()[1]);
        if (args.length == 1) {
            System.out.println(args[0]);
            return;
        }
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数 " + i + " = [" + args[i] + "]");
        }
    }

    public static void showLogInfo(Exception msg) {
        String detilErrMsg = " " + msg + "\n详情:     " + new Throwable().getStackTrace()[1] + "";
        System.out.println("报错信息:" + detilErrMsg);
        if (!offset) {
            return;
        }
        file("异常信息.txt", getStackTraceString(msg) + "\n详细信息:\n" + detilErrMsg);
    }

    public static void showLogInfo(String tag, Object msg) {
        String commonMsg = new Throwable().getStackTrace()[1] + "\n详情:   " + tag + ": " + msg;
        System.out.println("调用位置: " + commonMsg);
        if (!offset) {
            return;
        }
        file("普通信息.txt", msg.toString());
    }

    public static void showLogInfo(String msg) {
        System.out.println("调用位置: " + new Throwable().getStackTrace()[1] + "\n详情:   " + msg + "");
        if (!offset) {
            return;
        }
        file("普通信息.txt", msg);
    }

    /**
     * @param msg 日志信息
     */
    public static void file(String msg) {
        new Thread(() -> {
            String filePath =
                FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()
                    + File.separator + "日志信息.log";
            file(filePath, msg);
        }).start();
    }

    /**
     * @param filePath 日志文件路径
     * @param msg 日志信息
     */
    public static void file(String filePath, String msg) {
        File file = new File(filePath);
        BufferedWriter writer = null;
        try {
            if (!file.exists()) {
                boolean mkdirs = new File(file.getParent()).mkdirs();
                if (!file.createNewFile()) {
                    System.out.println("创建日志文件失败");
                }
            }
            writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true)));
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, false);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static void printExceptionInfo(Throwable e) {
        System.out.println(getStackTraceString(e));
    }

    public static void showStepLogInfo() {
        System.out.println(new Throwable().getStackTrace()[1] + "  步骤" + stepNumber++);
    }

    public static void getChild(String... args) {
        if (args.length == 1) {
            System.out.println(new Throwable().getStackTrace()[2] + args[0]);
        } else if (args.length > 1) {
            System.out.println(new Throwable().getStackTrace()[2]);
            for (int i = 0; i < args.length; i++) {
                System.out.println("参数 " + i + " = [" + args[i] + "]");
                file(args[i], "普通信息.txt");
            }
        }
    }

    public static void showArgsInfo(String msg) {
            String detilErrMsg = " " + msg + "\n详情:     " + new Throwable().getStackTrace()[1] + "";
            System.out.println("报错信息:" + detilErrMsg);
    }
}
