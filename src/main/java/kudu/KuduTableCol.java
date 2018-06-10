package kudu;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;

import java.util.List;

/**
 *  KuduTableCol kudu包装类，包装了一些传参值，方便进行一些相关表操作的传值
 *  @ tableName 表名
 *  @ tableSchema 表字段
 *  @ tableBuilder 创建表类
 */
public class KuduTableCol {
    private String tableName;
    private Schema tableSchema;
    private List tableBuilder;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Schema getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(List<ColumnSchema> cols) {
        this.tableSchema = new Schema(cols);
    }

    public List getTableBuilder() {
        return tableBuilder;
    }

    public void setTableBuilder(List tableBuilder) {
        this.tableBuilder = tableBuilder;
    }
}
