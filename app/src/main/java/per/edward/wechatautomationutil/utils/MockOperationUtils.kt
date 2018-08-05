package per.edward.wechatautomationutil.utils

import java.io.DataOutputStream

class MockOperationUtils {
    companion object {
        /**
         * 执行shell命令
         *
         * @param cmd
         */
        fun execShellCmd(cmd: String) {

            try {
                // 申请获取root权限，这一步很重要，不然会没有作用
                val process = Runtime.getRuntime().exec("su")
                // 获取输出流
                val outputStream = process.outputStream
                val dataOutputStream = DataOutputStream(
                        outputStream)
                dataOutputStream.writeBytes(cmd)
                dataOutputStream.flush()
                dataOutputStream.close()
                outputStream.close()
            } catch (t: Throwable) {
                t.printStackTrace()
                LogUtil.e(t.toString())
            }

        }
    }
}