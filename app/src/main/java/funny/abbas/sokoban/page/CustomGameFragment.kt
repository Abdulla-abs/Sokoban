package funny.abbas.sokoban.page

import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseBooleanArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import funny.abbas.sokoban.R
import funny.abbas.sokoban.databinding.FragmentCustomGameBinding
import funny.abbas.sokoban.page.vm.CustomGameViewModel
import funny.abbas.sokoban.view.GameControllerView
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CustomGameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentCustomGameBinding
    private val viewmodel: CustomGameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomGameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sokobanView = binding.sokoban

        sokobanView.setStateListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("提示")
                .setMessage("过关")
                .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()
                    }
                })
                .show()
        }

        binding.controller.setCallback(object : GameControllerView.GameControllerViewCallback() {
            override fun on(keyNumber: Int, keyName: String?, keyStatuses: SparseBooleanArray?) {
                when (keyNumber) {
                    GameControllerView.KEY_LEFT, GameControllerView.KEY_Y -> {
                        binding.sokoban.moveLeft()
                    }

                    GameControllerView.KEY_UP, GameControllerView.KEY_X -> {
                        binding.sokoban.moveTop()
                    }

                    GameControllerView.KEY_RIGHT, GameControllerView.KEY_A -> {
                        binding.sokoban.moveRight()
                    }

                    GameControllerView.KEY_DOWN, GameControllerView.KEY_B -> {
                        binding.sokoban.moveBottom()
                    }

                    GameControllerView.KEY_L -> {
                        viewmodel.onIntent(CustomIntent.PreviousLevel())
                    }

                    GameControllerView.KEY_R -> {
                        viewmodel.onIntent(CustomIntent.NextLevel())
                    }

                    GameControllerView.KEY_SELECT -> {
                        viewmodel.onIntent(CustomIntent.ReloadLevel())
                    }

                    GameControllerView.KEY_START -> {
                        binding.sokoban.backStep()
                    }
                }
            }

            override fun off(keyNumber: Int, keyName: String?, keyStatuses: SparseBooleanArray?) {
                super.off(keyNumber, keyName, keyStatuses)
            }
        })

        lifecycleScope.launch {
            viewmodel.uiState.collect { uiState ->
                when (uiState.dataState) {
                    is LevelListState.Success -> {
                        sokobanView.setLevel(uiState.currentLevel)
                    }

                    else -> {
                        //nothing
                    }
                }
                requireActivity().findViewById<View>(R.id.toolbar)?.let {
                    (it as Toolbar).apply {
                        subtitle = "第${uiState.currentIndex + 1}/${uiState.levelsSize}关"
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewmodel.sideEffect.collect { effect ->
                when (effect) {
                    is CustomEffect.ShowTips -> {
                        Toast.makeText(requireActivity(), effect.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
//        lifecycleScope.launch {
//            viewmodel.levelFlow.collect { levelResult ->
//                if (levelResult.isSuccess) {
//                    requireActivity().findViewById<View>(R.id.toolbar)?.let {
//                        (it as Toolbar).apply {
//                        subtitle = "第${viewmodel.currentIndex+1}关"
//                    }
//                    }
//
//                    binding.sokoban.setLevel(levelResult.getOrNull())
//                }
//                if (levelResult.isFailure) {
//                    Toast.makeText(
//                        requireActivity(), levelResult.exceptionOrNull().toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomGameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomGameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}