package com.alibaba.tailbase.clientprocess;

import com.alibaba.tailbase.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class ClientProcessDataTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProcessData.class.getName());



    @org.junit.Test
    public void testTimeLoadFile() {
        String dataFilePath = "C:\\vol\\robin\\pyproject\\data\\trace1.data";
        File file=new File(dataFilePath);
        InputStream input = null;
        try {
            input=new FileInputStream(file);
            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            String line;
            long count = 0;
            while ((line = bf.readLine()) != null) {
                count++;
            }
        } catch (Exception e) {
            LOGGER.error("File not found");
            return;
        }
    }

    @org.junit.Test
    public void testTimeLoadFileWithApache() {
        String dataFilePath = "C:\\vol\\robin\\pyproject\\data\\trace1.data";
        File file=new File(dataFilePath);
        try {
            LineIterator it = FileUtils.lineIterator(file);
            String line;
            long count = 0;
            while ((line = it.nextLine()) != null) {
                count++;
            }
        } catch (Exception e) {
            LOGGER.error("File not found");
            return;
        }
    }

    @org.junit.Test
    public void testTimeFilterFile() {
        String dataFilePath = "C:\\vol\\robin\\pyproject\\data\\trace1.data";
        // an list of trace map,like ring buffe.  key is traceId, value is spans ,  r
        List<Map<String,List<String>>> BATCH_TRACE_LIST = new ArrayList<>();
        // make 50 bucket to cache traceData
        int BATCH_COUNT = 15;
        for (int i = 0; i < BATCH_COUNT; i++) {
            BATCH_TRACE_LIST.add(new ConcurrentHashMap<>(Constants.BATCH_SIZE));
        }

        try {
            LOGGER.info("local data file path:" + dataFilePath);
            InputStream input = null;

            File file=new File(dataFilePath);
            try {
                input=new FileInputStream(file);
            } catch (FileNotFoundException e) {
                LOGGER.error("File not found");
                return;
            }

            BufferedReader bf = new BufferedReader(new InputStreamReader(input));
            String line;
            long count = 0;
            int pos = 0;
            Set<String> badTraceIdList = new HashSet<>(1000);
            Map<String, List<String>> traceMap = BATCH_TRACE_LIST.get(pos);
            while ((line = bf.readLine()) != null) {
                count++;
                String[] cols = line.split("\\|");
                if (cols != null && cols.length > 1 ) {
                    String traceId = cols[0];
                    List<String> spanList = traceMap.get(traceId);
                    if (spanList == null) {
                        spanList = new ArrayList<>();
                        traceMap.put(traceId, spanList);
                    }
                    spanList.add(line);
                    if (cols.length > 8) {
                        String tags = cols[8];
                        if (tags != null) {
                            if (tags.contains("error=1")) {
                                badTraceIdList.add(traceId);
                            } else if (tags.contains("http.status_code=") && tags.indexOf("http.status_code=200") < 0) {
                                badTraceIdList.add(traceId);
                            }
                        }
                    }
                }
                if (count % Constants.BATCH_SIZE == 0) {
                    pos++;
                    // loop cycle
                    if (pos >= BATCH_COUNT) {
                        pos = 0;
                    }
                    traceMap = BATCH_TRACE_LIST.get(pos);
                    // donot produce data, wait backend to consume data
                    // TODO to use lock/notify
                    if (traceMap.size() > 0) {
                        traceMap.clear();
                    }
                    // batchPos begin from 0, so need to minus 1
                    int batchPos = (int) count / Constants.BATCH_SIZE - 1;
                    //updateWrongTraceId(badTraceIdList, batchPos);
                    badTraceIdList.clear();
                    LOGGER.info("suc to updateBadTraceId, batchPos:" + batchPos);
                }
            }
            //updateWrongTraceId(badTraceIdList, (int) (count / Constants.BATCH_SIZE - 1));
            bf.close();
            input.close();
            //callFinish();
        } catch (Exception e) {
            LOGGER.warn("fail to process data", e);
        }
    }
}