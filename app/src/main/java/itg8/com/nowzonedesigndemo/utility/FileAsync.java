package itg8.com.nowzonedesigndemo.utility;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import itg8.com.nowzonedesigndemo.common.DataModel;


public class FileAsync extends AsyncTask<List<DataModel>, Void, Boolean> {

    private static final int ACTUAL_NUMBER_OF_COUNT_IN_FILE = 30;
    private static final String TAG = ServiceOnCheck.class.getSimpleName();
    private static String COMPLETE_FILE_PATH;
    private String[] fileList;

    public FileAsync(String path, String[] fileList) {
        COMPLETE_FILE_PATH = path;
        this.fileList = fileList;
    }

    @Override
    protected Boolean doInBackground(List<DataModel>... lists) {
        List<DataModel> dataModels = lists[0];
        storeToFile(dataModels);
        return true;
    }


    /**
     * We will store complete data to file as per conditions
     *
     * @param models
     */
    private void storeToFile(List<DataModel> models) {
        String content = new Gson().toJson(models);
        String[] allFileListByDt = getFileList();
        if (allFileListByDt.length > 0) {
            Arrays.sort(allFileListByDt);
            writeContentToFile(allFileListByDt[allFileListByDt.length - 1], content);
        } else {
            createFile(content, getIntFromTSMP(models.get(0).getTimestamp()) + ".txt", "0");
        }

    }

    //Timestamp is in full format. we dont want that much big . so eliminate millisecond
    private String getIntFromTSMP(long timestamp) {
        int count = 0;
        try {
            count = (int) (timestamp / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(count);
    }

    /**
     * this method creates a file which is provided by
     *
     * @param content  actual json content
     * @param fileName actual file name
     */
    private void createFile(String content, String fileName) {
        if (content != null && fileName != null)
            writeInFile(content, fileName);
    }

    /**
     * this method creates a file which is provided by
     *
     * @param content  actual json content
     * @param fileName actual file name
     * @param count    actual count of json available in file
     */
    private void createFile(String content, String fileName, String count) {
        String newFileName = count + "_" + fileName;
        writeInFile(content, newFileName);
    }

    private void writeInFile(String content, String newFileName) {
        File completeFileStructure = new File(COMPLETE_FILE_PATH + newFileName);
        try {
            FileWriter fWriter = new FileWriter(completeFileStructure);
            fWriter.append(content).append("\n");
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeContentToFile(String s, String models) {
        File newFileName = checkIfNewFileOrOldFileReplace(models);
        if (newFileName != null) {
            createFile(s, newFileName.getAbsolutePath());
        }
    }

    private File checkIfNewFileOrOldFileReplace(String models) {
        String[] getCountAndData = models.split("_");
        File oldFile = new File(models);
        File newFile = null;
        int count;
        if (getCountAndData.length > 1) {
            String countString = getCountAndData[0];
            count = Integer.parseInt(countString);
            if (count < ACTUAL_NUMBER_OF_COUNT_IN_FILE) {
                count += 1;
                newFile = new File(oldFile.getParentFile(), String.valueOf(count) + getCountAndData[1]);
                boolean b = oldFile.renameTo(newFile);
                if (b)
                    return oldFile;
            } else {
                newFile = new File(oldFile.getParentFile(), String.valueOf(count) + getCountAndData[1]);
                return newFile;
            }
        }
        return newFile;
    }

    private String[] getFileList() {

        return fileList;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
