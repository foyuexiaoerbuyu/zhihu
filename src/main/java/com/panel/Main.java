package com.panel;

import com.panel.custom.EditText;
import com.panel.custom.GridLayout;
import com.panel.custom.TextView;
import com.panel.custom.WindowAlert;
import com.zhihu.utils.ZhihuUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

//窗口          Stage
//  -场景       Scene
//    -布局     stackPane
//      -控件   Button
public class Main extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
//        HBox hbox = new HBox();
        TextView seltPath = new TextView("D:/知乎收藏/");
        Button seltPathBtn = new Button("选择路径");
        seltPathBtn.setOnMouseClicked(event -> {
            //选择保存文件夹路径
            seltPath.setText(new DirectoryChooser().showDialog(stage).getPath());
        });
        EditText articleEdt = new EditText("请输入文章链接");
        Button articleBtn = new Button("保存文章");
        articleBtn.setOnMouseClicked(event -> {
            //获取文章链接
            System.out.println("获取文章链接:" + articleEdt.getText());
            if (StringUtils.isBlank(articleEdt.getText())) {
                WindowAlert.display("警告", "链接不能为空");
                return;
            }
            ZhihuUtils.saveArticle(seltPath.getText(), articleEdt.getText());
        });

        EditText collectionEdt = new EditText("请输入收藏链接Id");
        Button collectionBtn = new Button("保存收藏");
        collectionBtn.setOnMouseClicked(event -> {
            //获取收藏id
            if (StringUtils.isBlank(collectionEdt.getText())) {
                WindowAlert.display("警告", "收藏id不能为空");
                return;
            }
            System.out.println("获取收藏id:" + collectionBtn.getText());
            ZhihuUtils.saveArticle(collectionBtn.getText(), collectionEdt.getText());
        });
        GridLayout gridLayout = new GridLayout(3, 2);
        gridLayout
            .addView(seltPath, seltPathBtn, articleEdt, articleBtn, collectionEdt, collectionBtn);
        stage.setScene(new Scene(gridLayout)); // 把水平箱子放到边界窗格的中央
        stage.show();
    }

    public void handleClose() {
        // 接收窗体返回值
        boolean ret = WindowAlert.display("关闭窗口", "是否关闭窗口？");
        System.out.println(ret);
        if (ret) {
            stage.close();
        }

    }
}

