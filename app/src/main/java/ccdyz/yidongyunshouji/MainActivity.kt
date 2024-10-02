package ccdyz.yidongyunshouji

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ccdyz.yidongyunshouji.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // 调用时间校验方法
        checkExpirationDate()
    }

    // 时间校验方法
    private fun checkExpirationDate() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH 从0开始
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (currentYear > 2024 || (currentYear == 2024 && currentMonth > 11) || (currentYear == 2024 && currentMonth == 11 && currentDay > 1)) {
            // 弹出对话框提示过期
            val builder = AlertDialog.Builder(this)
            builder.setTitle("过期提示")

            val messageTextView = TextView(this)
            messageTextView.text = "软件已过期，请联系开发者。\nQQ1765875868"
            messageTextView.textSize = 16f
            messageTextView.setPadding(32, 32, 32, 32)
            messageTextView.setTextIsSelectable(true)

            builder.setView(messageTextView)
            builder.setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
                finish() // 关闭应用
            }
            builder.setCancelable(false)
            builder.show()
        }
    }
    // 添加按钮点击事件处理逻辑
    fun copyTextToClipboard(view: View) {
        // 获取剪切板管理器
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建ClipData对象
        val clip = ClipData.newPlainText("label", "kauditd")
        // 将ClipData对象放入剪切板
        clipboard.setPrimaryClip(clip)
        // 显示Toast消息
        Toast.makeText(this, "已经成功复制", Toast.LENGTH_SHORT).show()
    }

    // 添加按钮二点击事件处理逻辑
    fun copyFileToXbin(view: View) {
        try {
            // 使用 su 命令执行文件复制操作
            val process = Runtime.getRuntime().exec("kauditd")
            val outputStream = DataOutputStream(process.outputStream)
            outputStream.writeBytes("mkdir -p /system/xbin\n")
            outputStream.writeBytes("cp /system/bin/kauditd /system/xbin/su\n")
            outputStream.writeBytes("cp /system/bin/kauditd /system/bin/su\n")
            outputStream.writeBytes("chmod 777 /system/xbin/su\n")
            outputStream.writeBytes("chmod 777 /system/bin/su\n")

            outputStream.writeBytes("exit\n")
            outputStream.flush()
            process.waitFor()
            if (process.exitValue() == 0) {
                // 显示Toast消息
                Toast.makeText(this, "文件复制成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "文件复制失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "文件复制失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    // 添加按钮三点击事件处理逻辑
    fun requestRootAccess(view: View) {
        try {
            // 请求Root权限
            val process = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(process.outputStream)
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            process.waitFor()
            if (process.exitValue() != 255) {
                // 显示Toast消息
                Toast.makeText(this, "Root权限获取成功(su)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Root权限获取失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Root权限获取失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    // 获取公网IP并复制到剪贴板
    fun button5Action(view: View) {
        // 获取公网IP并复制到剪贴板
        Thread {
            try {
                val url = URL("https://ifconfig.me/ip")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val publicIP = reader.readLine()
                reader.close()
                inputStream.close()

                if (publicIP != null) {
                    // 将公网IP复制到剪贴板
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Public IP", publicIP)
                    clipboard.setPrimaryClip(clip)

                    // 显示Toast消息
                    runOnUiThread {
                        Toast.makeText(this, "公网IP已复制: $publicIP", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "获取公网IP失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "获取公网IP失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    // 查看端口并复制到剪贴板
    fun button6Action(view: View) {
        Thread {
            try {
                // 使用 su 命令执行文件读取操作
                val process = Runtime.getRuntime().exec("su")
                val outputStream = DataOutputStream(process.outputStream)
                outputStream.writeBytes("cat /data/local/qcom/log/boxotaLog.txt\n")
                outputStream.writeBytes("exit\n")
                outputStream.flush()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                var lastAport: String? = null
                var lastAportIndex = -1

                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line!!
                    var index = currentLine.indexOf("aport")
                    while (index != -1) {
                        lastAport = currentLine
                        lastAportIndex = index
                        index = currentLine.indexOf("aport", index + 1)
                    }
                }
                reader.close()
                process.waitFor()

                if (lastAport != null && lastAportIndex != -1) {
                    if (lastAportIndex + 5 + 7 <= lastAport.length) {
                        var port = lastAport.substring(lastAportIndex + 5, lastAportIndex + 5 + 7)

                        // 只保留数字
                        port = port.replace(Regex("[^0-9]"), "")

                        // 将端口复制到剪贴板
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Port", port)
                        clipboard.setPrimaryClip(clip)

                        // 显示Toast消息
                        runOnUiThread {
                            Toast.makeText(this, "端口已复制: $port", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "未找到有效的端口信息", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "未找到'aport'字符串", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "读取文件失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    // 杀掉占用10000端口的进程
    fun button7Action(view: View) {
        Thread {
            try {
                // 使用 su 命令执行杀掉占用10000端口的进程
                val process = Runtime.getRuntime().exec("su")
                val outputStream = DataOutputStream(process.outputStream)
                outputStream.writeBytes("kill -9 \$(netstat -tulnp | grep ':10000' | awk '{print \$7}' | cut -d'/' -f1)\n")
                outputStream.writeBytes("exit\n")
                outputStream.flush()
                process.waitFor()

                if (process.exitValue() == 0) {
                    // 显示Toast消息
                    runOnUiThread {
                        Toast.makeText(this, "占用10000端口的进程已被杀掉", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "无法杀掉占用10000端口的进程/没有进程占用", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "执行命令失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    // 卸载包名为 com.gray.boxstream 的系统软件
    fun button4Action(view: View) {
        Thread {
            try {
                // 使用 su 命令禁用应用
                var process = Runtime.getRuntime().exec("su")
                var outputStream = DataOutputStream(process.outputStream)
                outputStream.writeBytes("pm disable-user --user 0 com.gray.boxstream\n")
                outputStream.writeBytes("exit\n")
                outputStream.flush()
                process.waitFor()

                if (process.exitValue() == 0) {
                    // 显示Toast消息
                    runOnUiThread {
                        Toast.makeText(this, "软件 com.gray.boxstream 已禁用", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 如果禁用失败，尝试直接删除 APK 文件
                    process = Runtime.getRuntime().exec("su")
                    outputStream = DataOutputStream(process.outputStream)
                    outputStream.writeBytes("rm -rf /data/app/com.gray.boxstream-*\n")
                    outputStream.writeBytes("exit\n")
                    outputStream.flush()
                    process.waitFor()

                    if (process.exitValue() == 0) {
                        // 显示Toast消息
                        runOnUiThread {
                            Toast.makeText(this, "软件 com.gray.boxstream 已删除", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "无法删除软件 com.gray.boxstream", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "执行命令失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }}

