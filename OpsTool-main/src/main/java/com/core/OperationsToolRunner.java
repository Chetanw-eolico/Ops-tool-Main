package com.core;

import java.sql.SQLException;

import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.core.windows.LoginWindow;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
public class OperationsToolRunner implements CommandLineRunner {

	@Autowired
	DataSource dataSource;
    public static void main(String[] args) throws Exception {

        //disabled banner, don't want to see the spring logo
        SpringApplication app = new SpringApplication(OperationsToolRunner.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setHeadless(false);
        app.run(args);

        //SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Login to Operations Tool");
				LoginWindow loginWindow = null;
			    try {
					loginWindow = new LoginWindow(frame, new JdbcTemplate(dataSource));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				loginWindow.setVisible(true);
			}
		});
    }
}