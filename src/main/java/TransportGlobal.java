import kudu.KuduClientJava;
import project.h58pic.H58pic_Img_Down_Kudu;
import project.h58pic.H58pic_Img_Kudu;

public class TransportGlobal {
    public static void main(String[] args) {
        KuduClientJava kuduClient = new H58pic_Img_Kudu();
        kuduClient.getConnection();

        String tableName = "access_58pic_log";
//        String tableName = "access_58pic_log_img";
//        String tableName = "access_58pic_log_img_down_favorites";
        //建表
//        kuduClient.createKuduTable(kuduClient.createTableWay(tableName));
        //删除kudu表
//        kuduClient.deleteKuduTable(tableName);
        //展示表
//        kuduClient.showTables();
        //插入测试
//        kuduClient.testInsert(tableName);
//        批量插入数据
        kuduClient.insertRowDataByBatch("web6",tableName,1,1);
        //删除数据
//        kuduClient.deleteRowData(tableName,1,2);
        //查询数据
//        kuduClient.scanSpecificColumn(tableName);
    }
}
