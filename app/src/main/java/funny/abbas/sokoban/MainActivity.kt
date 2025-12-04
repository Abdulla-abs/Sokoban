package funny.abbas.sokoban

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import funny.abbas.sokoban.databinding.ActivityMainBinding
import funny.abbas.sokoban.domain.Box
import funny.abbas.sokoban.domain.BoxType
import funny.abbas.sokoban.domain.Level
import funny.abbas.sokoban.domain.LevelMapper
import funny.abbas.sokoban.domain.Location
import funny.abbas.sokoban.domain.Role
import funny.abbas.sokoban.view.SokobanView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        val map = arrayOf(
            arrayOf(BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Empty,BoxType.Empty,BoxType.Empty,BoxType.Empty,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Empty,BoxType.Wall,BoxType.Role,BoxType.Empty,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Empty,BoxType.Empty,BoxType.Box,BoxType.Empty,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Empty,BoxType.Box,BoxType.Box,BoxType.Empty,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Empty,BoxType.Empty,BoxType.Empty,BoxType.Empty,BoxType.Wall),
            arrayOf(BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall,BoxType.Wall)
        )

        val sokobanView = binding.sokoban
        sokobanView.setLevel(LevelMapper.mapper(map))


        binding.btMoveLeft.setOnClickListener {
            sokobanView.controller.moveLeft()
        }
        binding.btMoveUp.setOnClickListener {
            sokobanView.controller.moveTop()
        }
        binding.btMoveRight.setOnClickListener {
            sokobanView.controller.moveRight()
        }
        binding.btMoveBottom.setOnClickListener {
            sokobanView.controller.moveBottom()
        }


    }

    private fun showDebug(role: Role){
//        binding.debug.text = "Position:${role.location.x}:${role.location.y} " +
//                "[Left: ${role.canMoveLeft()}]" +
//                "[Top: ${role.canMoveTop()}]" +
//                "[Right: ${role.canMoveRight()}]" +
//                "[Bottom: ${role.canMoveBottom()}]"
    }
}