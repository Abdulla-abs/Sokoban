package funny.abbas.sokoban

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import funny.abbas.sokoban.databinding.ActivityMainBinding
import funny.abbas.sokoban.page.vm.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
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
        navController = navHostFragment.navController


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
            onNavigationClick(menuItem)
            // 点击后自动关掉侧滑菜单（推荐）
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            binding.toolbar.subtitle = null
            true
        }

    }

    private fun onNavigationClick(menuItem: MenuItem) {
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

            R.id.about -> {
                navController.navigate(R.id.aboutFragment)
            }
        }
    }


    override fun onBackPressed() {
        binding.drawerLayout?.let {
            if (it.isDrawerOpen(GravityCompat.START)) {
                it.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }

    }

}