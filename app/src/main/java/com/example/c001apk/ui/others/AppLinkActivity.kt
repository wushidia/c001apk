package com.example.c001apk.ui.others

import android.net.Uri
import android.os.Bundle
import com.example.c001apk.ui.feed.FeedActivity
import com.example.c001apk.ui.user.UserActivity
import com.example.c001apk.util.IntentUtil
import com.example.c001apk.util.NetWorkUtil.openLink
import rikka.material.app.MaterialActivity

class AppLinkActivity : MaterialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data // 获取 Intent 中的 Uri 对象

        if (uri != null) {
            val scheme = uri.scheme // 获取协议部分 (coolmarket 或 https)
            val host = uri.host // 获取主机名部分 (www.coolapk.com 或空)
            val path = uri.path // 获取路径部分 (/feed/67058122 或 /u/123)
            
            when {
                // 处理 coolmarket:// 协议的链接
                scheme == "coolmarket" -> {
                    when {
                        path?.startsWith("/feed/") == true -> {
                            val id = path.removePrefix("/feed/")
                            IntentUtil.startActivity<FeedActivity>(this) {
                                putExtra("id", id)
                            }
                        }
                        path?.startsWith("/u/") == true -> {
                            val id = path.removePrefix("/u/")
                            IntentUtil.startActivity<UserActivity>(this) {
                                putExtra("id", id)
                            }
                        }
                        else -> {
                            openLink(this, uri.toString(), null)
                        }
                    }
                }
                
                // 处理 https:// 协议的链接
                scheme == "https" && (host == "www.coolapk.com" || host == "coolapk.com") -> {
                    when {
                        path?.startsWith("/feed/") == true -> {
                            // 提取ID
                            val idWithParams = path.removePrefix("/feed/")
                            // 分离ID和查询参数
                            val id = idWithParams.substringBefore("?")
                            IntentUtil.startActivity<FeedActivity>(this) {
                                putExtra("id", id)
                            }
                        }
                        path?.startsWith("/u/") == true -> {
                            val idWithParams = path.removePrefix("/u/")
                            val id = idWithParams.substringBefore("?")
                            IntentUtil.startActivity<UserActivity>(this) {
                                putExtra("id", id)
                            }
                        }
                        else -> {
                            openLink(this, uri.toString(), null)
                        }
                    }
                }
                
                // 其他协议或域名的链接
                else -> {
                    openLink(this, uri.toString(), null)
                }
            }
        } else {
            // 如果 Uri 为 null，直接结束 Activity
        }

        finish() // 处理完成后立即结束 Activity
    }
}
