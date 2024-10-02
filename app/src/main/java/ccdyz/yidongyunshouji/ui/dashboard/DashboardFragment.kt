package ccdyz.yidongyunshouji.ui.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ccdyz.yidongyunshouji.databinding.FragmentDashboardBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 添加按钮点击事件处理逻辑
    fun button4Action(view: View) {
        Toast.makeText(requireContext(), "卸载占用软件", Toast.LENGTH_SHORT).show()
    }

    fun button5Action(view: View) {
        // 获取公网IP并复制到剪贴板
        Thread {
            try {
                val url = URL("https://api.ipify.org")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val publicIP = reader.readLine()
                reader.close()
                inputStream.close()

                // 将公网IP复制到剪贴板
                val clipboard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Public IP", publicIP)
                clipboard.setPrimaryClip(clip)

                // 显示Toast消息
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "公网IP已复制: $publicIP", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "获取公网IP失败: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }

    fun button7Action(view: View) {}


}