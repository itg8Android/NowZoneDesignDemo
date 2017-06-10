package itg8.com.nowzonedesigndemo.utility;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import itg8.com.nowzonedesigndemo.common.DataModel;
import timber.log.Timber;


public class FileAsync extends AsyncTask<List<DataModel>, Void, Boolean> {

    public static final Object[] DATA_LOCK = new Object[0];
    private static final int ACTUAL_NUMBER_OF_COUNT_IN_FILE = 30;
    private static final String TAG = ServiceOnCheck.class.getSimpleName();
    private static String COMPLETE_FILE_PATH;
    private String[] fileList;

    public FileAsync(String path) {
        COMPLETE_FILE_PATH = path;
        File file = new File(path);
        if (file.exists()) {
            fileList = file.list();
        } else {
            fileList = new String[0];
        }
    }

    /**
     * Append String to end of File.
     *
     * @param appendContents
     * @param file
     * @return
     */
    public static boolean appendStringToFile(final String appendContents, final File file) {
        boolean result = false;
        try {
            synchronized (DATA_LOCK) {
                if (file != null && file.canWrite()) {
                    boolean b = file.createNewFile(); // ok if returns false, overwrite
                    Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                    out.write(appendContents);
                    out.close();
                    result = true;
                }
            }
        } catch (IOException e) {
            Timber.d(e.getMessage());
            //   Log.e(Constants.LOG_TAG, "Error appending string data to file " + e.getMessage(), e);
        }
        return result;
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(List<DataModel>... lists) {
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
        /**
         * We are first storing json in file. But now we will create simple text file
         * TODO uncomment after SAAS
         */
//        String content = new Gson().toJson(models);

        String content = createDataStructureFromModel(models);
        String[] allFileListByDt = getFileList();
        if (allFileListByDt.length > 0) {
            Arrays.sort(allFileListByDt);
            writeContentToFile(content, allFileListByDt[allFileListByDt.length - 1]);
        } else {
            createFile(content, getIntFromTSMP(models.get(0).getTimestamp()) + ".txt", "0");
        }

    }

    private String createDataStructureFromModel(List<DataModel> models) {
        String data = "";
        for (DataModel model :
                models) {
            data = data + model.getTimestamp() + " " + model.getPressure() + " " + model.getX() + " " + model.getY() + " " + model.getZ() + " " + model.getBattery() + " " + model.getTemprature() + "\n";
        }
        return data;
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
        writeInFile(content, COMPLETE_FILE_PATH + File.separator + newFileName);
    }

    private void writeInFile(String content, String newFileName) {
        File completeFileStructure = new File(newFileName);
        try {
        FileWriter fWriter = new FileWriter(completeFileStructure, true);
        fWriter.append(content).append("\n");
        fWriter.flush();
        fWriter.close();
                } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Got file having data from http://www.java2s.com/Code/Android/File/Writeandappendstringtofile.htm
         */
//        boolean b=appendStringToFile(content,completeFileStructure);
//            Timber.d("FILE APPEND: "+b);
//        try {
        /**
         * this is my old method. Its creating same file override data
         */
//            FileWriter fWriter = new FileWriter(completeFileStructure,true);
//            fWriter.append(content).append("\n");
//            fWriter.flush();
//            fWriter.close();

//            FileOutputStream foutStream=context.openFileOutput(completeFileStructure.getName(), Context.MODE_APPEND);
        /**
         * New information from internet, @FileOutputStream true means append
         */
//            FileOutputStream fileOutputStream=new FileOutputStream(completeFileStructure,true);
//            OutputStreamWriter writer=new OutputStreamWriter(fileOutputStream);
//            writer.write(content);
//            writer.flush();
//            writer.close();

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void writeContentToFile(String s, String models) {
        File newFileName = checkIfNewFileOrOldFileReplace(models);
        if (newFileName != null) {
            createFile(s, newFileName.getAbsolutePath());
        }
    }

    private File checkIfNewFileOrOldFileReplace(String models) {
        String[] getCountAndData = models.split("_");
        File oldFile = new File(COMPLETE_FILE_PATH, models);
        File newFile = null;
        int count;
        if (getCountAndData.length > 1) {
            String countString = getCountAndData[0];
            count = Integer.parseInt(countString);
            if (count < ACTUAL_NUMBER_OF_COUNT_IN_FILE) {
                count += 1;
                newFile = new File(COMPLETE_FILE_PATH, String.valueOf(count) + "_" + getCountAndData[1]);
                boolean b = oldFile.renameTo(newFile);
                if (b)
                    return oldFile;
            } else {
                count = 0;
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
