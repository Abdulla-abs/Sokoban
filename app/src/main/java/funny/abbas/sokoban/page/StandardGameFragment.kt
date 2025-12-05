package funny.abbas.sokoban.page

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import funny.abbas.sokoban.MainActivity
import funny.abbas.sokoban.R
import funny.abbas.sokoban.databinding.FragmentStandardGameBinding
import funny.abbas.sokoban.domain.StandardLevelBoard

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StandardGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StandardGameFragment : Fragment() {
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

    private lateinit var binding: FragmentStandardGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStandardGameBinding.inflate(inflater, container, false)
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

        binding.btMoveLeft.setOnClickListener {
            sokobanView.moveLeft()
        }
        binding.btMoveUp.setOnClickListener {
            sokobanView.moveTop()
        }
        binding.btMoveRight.setOnClickListener {
            sokobanView.moveRight()
        }
        binding.btMoveBottom.setOnClickListener {
            sokobanView.moveBottom()
        }

        sokobanView.setLevel(StandardLevelBoard().level1())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StandardGameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StandardGameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}