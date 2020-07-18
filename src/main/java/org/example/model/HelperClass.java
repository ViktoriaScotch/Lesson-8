package org.example.model;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Вспомогательный класс
 */
public class HelperClass {
    /**
     *
      * @return - дату в данный момент для формата swagger
     */
    public static String getActualData()    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+0000");
        Date data = new Date();
        String actualData = format.format(data);
        return parseData(actualData);
    }

    /**
     *
     * @param textToSplit - дата в неправильном формате
     * @return - дату в формате для swagger
     */
    private static String parseData(String textToSplit)  {
        String[] array = new String[2];
        array = textToSplit.split(" ");
        return array[0] + "T" + array[1];
    }

}
