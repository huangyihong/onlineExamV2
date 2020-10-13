package cmp;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

public class MysqlDoc {
	@Test
	public void shouldAnswerWithTrue() {
	    //数据源
	    HikariConfig hikariConfig = new HikariConfig();
	    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/online_exam_v2?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8");
	    hikariConfig.setUsername("root");
	    hikariConfig.setPassword("123456");
	    //设置可以获取tables remarks信息
	    hikariConfig.addDataSourceProperty("useInformationSchema", "true");
	    hikariConfig.setMinimumIdle(2);
	    hikariConfig.setMaximumPoolSize(5);
	    DataSource dataSource = new HikariDataSource(hikariConfig);
	    //生成配置
	    EngineConfig engineConfig = EngineConfig.builder()
	            //生成文件路径
	            .fileOutputDir("E:\\upload")
	            //打开目录
	            .openOutputDir(true)
	            //文件类型
	            .fileType(EngineFileType.HTML)
	            //生成模板实现
	            .produceType(EngineTemplateType.freemarker).build();

	    //忽略表
	    ArrayList<String> ignoreTableName = new ArrayList<>();
	    ignoreTableName.add("test_user");
	    ignoreTableName.add("test_group");
	    //忽略表前缀
	    ArrayList<String> ignorePrefix = new ArrayList<>();
	    ignorePrefix.add("test_");
	    //忽略表后缀
	    ArrayList<String> ignoreSuffix = new ArrayList<>();
	    ignoreSuffix.add("_test");
	    ProcessConfig processConfig = ProcessConfig.builder()
	            //忽略表名
	            .ignoreTableName(ignoreTableName)
	            //忽略表前缀
	            .ignoreTablePrefix(ignorePrefix)
	            //忽略表后缀
	            .ignoreTableSuffix(ignoreSuffix).build();
	    //配置
	    Configuration config = Configuration.builder()
	            //版本
	            .version("1.0.0")
	            //描述
	            .description("数据库设计文档生成")
	            //数据源
	            .dataSource(dataSource)
	            //生成配置
	            .engineConfig(engineConfig)
	            //生成配置
	            .produceConfig(processConfig).build();
	    //执行生成
	    new DocumentationExecute(config).execute();
	}
}