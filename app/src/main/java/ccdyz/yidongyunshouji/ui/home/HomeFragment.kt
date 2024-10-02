package ccdyz.yidongyunshouji.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ccdyz.yidongyunshouji.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//    fun copyTextToClipboard(view: View) {
//        // 获取剪切板管理器
//        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        // 创建ClipData对象
//        val clip = ClipData.newPlainText("label", "kauditd")
//        // 将ClipData对象放入剪切板
//        clipboard.setPrimaryClip(clip)
//        // 显示Toast消息
//        Toast.makeText(this, "已经成功复制", Toast.LENGTH_SHORT).show()
//    }
}
















