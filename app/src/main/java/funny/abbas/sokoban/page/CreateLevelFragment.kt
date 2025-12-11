package funny.abbas.sokoban.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import funny.abbas.sokoban.MyApplication
import funny.abbas.sokoban.R
import funny.abbas.sokoban.database.bean.CustomLevel
import funny.abbas.sokoban.databinding.FragmentCreateLevelBinding
import funny.abbas.sokoban.core.CreateSokobanStateMachine
import funny.abbas.sokoban.state.createsokoban.CreateSokobanAction
import funny.abbas.sokoban.state.createsokoban.CreateSokobanState
import funny.abbas.sokoban.util.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateLevelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateLevelFragment : Fragment() {
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

    private lateinit var binding: FragmentCreateLevelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateLevelBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.previousState.setOnClickListener {
            binding.createView.preState()
        }
        binding.nextState.setOnClickListener {
            binding.createView.nextState()
        }
        binding.addRow.setOnClickListener {
            binding.createView.addRow()
        }
        binding.addColumn.setOnClickListener {
            binding.createView.addColumn()
        }
        binding.reduceRow.setOnClickListener {
            binding.createView.reduceRow()
        }
        binding.reduceColumn.setOnClickListener {
            binding.createView.reduceColumn()
        }
        binding.createView.setListener { newState ->
            initCreateViewState(newState)
        }

        binding.save.setOnClickListener {
            val result = binding.createView.result
            if (result is Result.Success) {
                var serialize = result.data.serialize()
                val customLevel = CustomLevel()
                val level = customLevel.apply {
                    data = serialize
                    changeTime = Date()
                }
                val subscribe = Observable.fromAction<Unit> {
                    MyApplication.appDatabase.customLevelDao
                        .saveCustomLevel(level)
                }.subscribeOn(Schedulers.io())
                    .subscribe({}, {})
            } else if (result is Result.Failure) {
                Toast.makeText(requireContext(), result.e.message, Toast.LENGTH_SHORT).show()
            }

        }

        initCreateViewState(binding.createView.currentState())
    }

    private fun initCreateViewState(newState: CreateSokobanState){
        val canBeNextStateOp =
            CreateSokobanStateMachine.getTargetStatus(newState, CreateSokobanAction.TRANSFORM)
        val canBePreStateOp =
            CreateSokobanStateMachine.getTargetStatus(newState, CreateSokobanAction.BACK)
        requireActivity().findViewById<View>(R.id.toolbar)?.let {
            (it as Toolbar).apply {
            subtitle = "${newState.desc}"
        }
        }

        binding.previousState.isEnabled = canBePreStateOp != null
        binding.nextState.isEnabled = canBeNextStateOp != null
        binding.save.isEnabled = newState == CreateSokobanState.PUT_ROLE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateLevelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateLevelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}