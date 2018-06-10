package kudu;

import domain.BaseTable;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolsUtil.ArrayUtil;
import toolsUtil.FileUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * KuduClientJava：kudu的操作主类
 *
 * @ LOG ：设置日志输出，便于调试
 * @ DEFAULT_SLEEP ：主机端建立连接时候的defaultAdminOperationTimeoutMs(DEFAULT_SLEEP)参数
 * @ KUDU_MASTER ： kuduMasterAddrass
 * @ listTablesResponse ： kuduTable List类，用于 show tables;
 * @ kuduTableCol ： 个人的kudu包装类--可以继续优化
 */
public abstract class KuduClientJava<T extends BaseTable> implements KuduTableWay<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(KuduClientJava.class);
    protected static final int DEFAULT_SLEEP = 600000; //defaultValue=50000
    protected static final String KUDU_MASTER =
            System.getProperty("kuduMaster", "192.168.1.31,192.168.1.32,192.168.1.33");
    public static KuduClient syncClient;
    protected static ListTablesResponse listTablesResponse;


    //--------------------------------常用的方法类--------------------------------------------
    //0.和master主机端建立连接
    @Override
    public void getConnection() {
        //Creates a new client that connects to the masters.
        syncClient = new KuduClient.KuduClientBuilder(KUDU_MASTER).defaultAdminOperationTimeoutMs(DEFAULT_SLEEP).build();
    }

    //1.0 show tables;
    @Override
    public void showTables() {
        try {
            listTablesResponse = syncClient.getTablesList();
        } catch (KuduException e) {
            e.printStackTrace();
            LOG.error("ExceptionInformation== ", e);
        }
        for (String tableString : listTablesResponse.getTablesList()) {
            System.out.println(tableString);
        }
    }

    //2.0 创建表
    @Override
    public void createKuduTable(KuduTableCol kuduTableCol) {
        try {
            if (syncClient.tableExists(kuduTableCol.getTableName())) {
                return;
            }
            //这里是建表语句，注意，第三个分区选项有时候也需要修改
            syncClient.createTable(kuduTableCol.getTableName(), kuduTableCol.getTableSchema(), new CreateTableOptions().setRangePartitionColumns(kuduTableCol.getTableBuilder()));
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    //2.1 创建表方法-示例
    @Override
    public KuduTableCol createTableWay(String kuduTableName) {
        //2.1.1 设置table表名
        KuduTableCol kuduTableCol = new KuduTableCol();
        kuduTableCol.setTableName(kuduTableName);
        //2.2.2 设置colmun
        //
        // 表字段
        /**
         * ("字段名",字段类型).key(boolean 是否为行键).encoding(字符编码).build() --构建
         */
        List<ColumnSchema> cols = new ArrayList<ColumnSchema>();
        Class c = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class father_c = c.getSuperclass();
        Field[] fields = c.getDeclaredFields();
        Field[] father_fields = father_c.getDeclaredFields();
        Field[] fields_all = ArrayUtil.concatAll(fields, father_fields);
        List<Field> keyf = new ArrayList<>();
        List<Field> columnf = new ArrayList<>();
        List<String> rangeKeys = new ArrayList();

        for (Field f : fields_all) {
            Annotation[] annotations = f.getAnnotations();
            for (Annotation a : annotations) {
                if (a.annotationType() == Key.class) {
                    keyf.add(f);
                }
                if (a.annotationType() == ImpalaColumn.class) {
                    columnf.add(f);
                }
            }
        }

        for (Field f : keyf) {
            Key k = f.getAnnotation(Key.class);
            cols.add(new ColumnSchema.ColumnSchemaBuilder(f.getName(), k.type()).key(true).build());
            if (k.rangeKey()) {
                rangeKeys.add(f.getName());
            }
        }

        for (Field f : columnf) {
            ImpalaColumn ic = f.getAnnotation(ImpalaColumn.class);
            cols.add(new ColumnSchema.ColumnSchemaBuilder(f.getName(), ic.type()).build());
        }

        kuduTableCol.setTableSchema(cols);
        //2.2.3 设置分区
        kuduTableCol.setTableBuilder(rangeKeys);
        return kuduTableCol;
    }

    //3.0删除kudu表操作
    @Override
    public void deleteKuduTable(String kuduTableName) {
        try {
            if (!syncClient.tableExists(kuduTableName)) {
                return;
            }
            this.syncClient.deleteTable(kuduTableName);
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    //4.0插入数据-默认提交
    @Override
    public void insertRowDataByDefault(T t, String tableName) {
        try {

            KuduSession session = this.syncClient.newSession();
            Insert insert = insertBuilder(t, tableName);
            session.apply(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Insert insertBuilder(T t, String tableName) throws KuduException, IllegalAccessException {
        KuduTable table = this.syncClient.openTable(tableName);
        Insert insert = table.newInsert();
        Field[] fields = t.getClass().getDeclaredFields();
        Field[] father_fields = t.getClass().getSuperclass().getDeclaredFields();
        Field[] fields_all = ArrayUtil.concatAll(fields, father_fields);
        for (Field f : fields_all) {
            f.setAccessible(true);
            switch (f.getGenericType().toString()) {
                case "class java.lang.String":
                    insert.getRow().addString(f.getName(), (String) f.get(t));
                    break;
                case "class java.lang.Double":
                    insert.getRow().addDouble(f.getName(), (Double) f.get(t));
                    break;
                case "class java.lang.ByteBuffer":
                    insert.getRow().addBinary(f.getName(), (ByteBuffer) f.get(t));
                    break;
                case "class java.lang.Boolean":
                    insert.getRow().addBoolean(f.getName(), (Boolean) f.get(t));
                    break;
                case "class java.lang.Float":
                    insert.getRow().addFloat(f.getName(), (Float) f.get(t));
                    break;
                case "class java.lang.Integer":
                    insert.getRow().addInt(f.getName(), (Integer) f.get(t));
                    break;
                case "class java.lang.Long":
                    insert.getRow().addLong(f.getName(), (Long) f.get(t));
                    break;
                case "class java.lang.Short":
                    insert.getRow().addShort(f.getName(), (Short) f.get(t));
                    break;
            }
        }
        return insert;
    }

    //5.0删除行数据
    @Override
    public void deleteRowData(String tableName, int indexOfStart, int indexOfEnd) {
        try {
            KuduSession session = this.syncClient.newSession();
            KuduTable table = this.syncClient.openTable(tableName);
            for (int i = indexOfStart; i < indexOfEnd; i++) {
                Delete delete = table.newDelete();
                delete.getRow().addString("date_time", "20180423");
                delete.getRow().addInt("line_number", i);
                delete.getRow().addInt("qt_uid", i);
                delete.getRow().addInt("qt_visitor", i);
                session.apply(delete);
            }
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    //6.0更新数据：列出所有主键，然后列出要更新的数据列即可
    @Override
    public void updateColumns(String tableName) {
        try {
            KuduSession session = this.syncClient.newSession();
            KuduTable table = this.syncClient.openTable(tableName);
            Update update = table.newUpdate();
            update.getRow().addInt("id", 1);
            update.getRow().addString("name", "张三大大");
            session.apply(update);
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    //8.0添加非主键列字段
    @Override
    public void alterTableAddColumn(String kuduTableName, String columnName, Type columnType, Object defaultVal) {
        try {
            KuduTable table = this.syncClient.openTable(kuduTableName);
            this.syncClient.alterTable(kuduTableName, new AlterTableOptions().addColumn(columnName, columnType, defaultVal));
            System.out.println("添加表字段 " + columnName + " 成功");
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }

    public void insertRowDataByBatch(String filePath, String tableName, int pointLog, int pointTable) {
        KuduSession session = this.syncClient.newSession();
        //采取Flush方式 手动刷新
        session.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND);
//        session.setMutationBufferSpace(3000);
        int i = pointLog;
        List<String> files = FileUtil.getFilePath(filePath, true);
        for (String file : files) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String len = "";
                while ((len = br.readLine()) != null) {
                    if (i++ < pointTable) continue;
                    T t = dataProcess(len);
                    if (t == null) {
                        continue;
                    }
                    t.setLine_number(i);
                    try {
                        Insert insert = insertBuilder(t, tableName);
                        session.apply(insert);
//                        if (i % 100 == 0) {
//                            session.flush();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        try {
//            session.flush();
//        } catch (KuduException e) {
//            e.printStackTrace();
//        }
    }

    public abstract void testInsert(String tableName);
}