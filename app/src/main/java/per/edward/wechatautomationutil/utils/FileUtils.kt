package per.edward.wechatautomationutil.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class FileUtils {
    companion object {
        fun readTxtFile(filePath: File):String{
            var stringBuffer=StringBuffer()
            var fileReader: InputStreamReader? = null
            var bufferedReader: BufferedReader? = null
            try {
                val inputStream = FileInputStream(filePath)
                fileReader = InputStreamReader(inputStream,"GBK")
                bufferedReader = BufferedReader(fileReader)
                try {
                    var read: String? = null
                    while ({ read = bufferedReader.readLine();read }() != null) {
                        stringBuffer.append(read?.trim()+"\n")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close()
                }
                if (fileReader != null) {
                    fileReader.close()
                }
            }
            return stringBuffer.toString()
        }

        fun isExistFile() {
            
        }
    }
}