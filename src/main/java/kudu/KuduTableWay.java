package kudu;

import org.apache.kudu.Type;
import project.h58pic.H58pic;

public interface KuduTableWay<T> {
    //建立连接
    void getConnection();

    //展示所有表
    void showTables();

    //建表语句准备
    KuduTableCol createTableWay(String kuduTableName);

    //执行建表操作
    void createKuduTable(KuduTableCol kuduTableCol);

    //执行删除表操作
    void deleteKuduTable(String kuduTableName);

    //数据预处理
    T dataProcess(String originalStr);

    //添加/插入行数据-默认
    void insertRowDataByDefault(T t,String tableName);

    //删除行数据
    void deleteRowData(String tableName, int indexOfStart, int indexOfEnd);

    //更新数据(update)
    void updateColumns(String tableName);

    //添加非主键列字段
    void alterTableAddColumn(String kuduTableName, String columnName, Type columnType, Object defaultVal);
}
