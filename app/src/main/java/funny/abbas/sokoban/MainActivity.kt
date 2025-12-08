package funny.abbas.sokoban

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import funny.abbas.sokoban.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        // 关键：把 Toolbar 左上角变成“三横”图标，并自动处理点击打开抽屉
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,    // 可不写，随便填
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()   // 这一行很重要！同步状态



        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 关键：告诉系统哪些是“顶级页面”（返回箭头会变成三横）
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.standardGameFragment,
                R.id.customGameFragment,
                R.id.createLevelFragment
            ), // 这里写所有顶级页面
            binding.drawerLayout
        )

// 自动处理 Toolbar 和抽屉图标
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 侧滑菜单点击自动跳转
        binding.navView.setupWithNavController(navController)

        // NavigationView 菜单点击事件
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.standard_level -> { /* 切换到首页 */
                    navController.navigate(R.id.standardGameFragment)
                }

                R.id.custom_level -> { /* 打开设置 */
                    navController.navigate(R.id.customGameFragment)
                }

                R.id.create_level -> { /* 退出登录 */
                    navController.navigate(R.id.createLevelFragment)
                }

                R.id.setting -> {
                    navController.navigate(R.id.settingsFragment)
                }
            }
            // 点击后自动关掉侧滑菜单（推荐）
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}