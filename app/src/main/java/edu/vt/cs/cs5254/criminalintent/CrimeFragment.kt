package edu.vt.cs.cs5254.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.vt.cs.cs5254.criminalintent.databinding.FragmentCrimeBinding

private const val TAG = "CrimeFragment"

class CrimeFragment : Fragment() {
    private var _binding: FragmentCrimeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CrimeViewModel by lazy {
        ViewModelProvider(this).get(CrimeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Crime fragment created for crime with ID ${viewModel.crime.id}")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.crimeDate.apply {
            text = viewModel.crime.date.toString()
            isEnabled = false
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(sequence: CharSequence?,
                                       start: Int, before: Int, count: Int) {
                viewModel.crime.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) { }
        }
        binding.crimeTitle.addTextChangedListener(titleWatcher)
        binding.crimeSolved.apply {
            setOnCheckedChangeListener { _, isChecked ->
                viewModel.crime.isSolved = isChecked
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CrimeFragment {
            return CrimeFragment()
        }
    }
}

//BNR code before following Dr K's Q&A
//
//class CrimeFragment : Fragment() {
//
//    private lateinit var crime: Crime
//    private lateinit var titleField: EditText
//    private lateinit var dateButton: Button
//    private lateinit var solvedCheckBox: CheckBox
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        crime = Crime()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_crime, container, false)
//
//        titleField = view.findViewById(R.id.crime_title) as EditText
//        dateButton = view.findViewById(R.id.crime_date) as Button
//        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
//        dateButton.apply {
//            text = crime.date.toString()
//            isEnabled = false
//        }
//
//        return view
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val titleWatcher = object : TextWatcher {
//            override fun beforeTextChanged(
//                sequence: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//
//            }
//            override fun onTextChanged(
//                sequence: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                crime.title = sequence.toString()
//            }
//            override fun afterTextChanged(sequence: Editable?) {
//
//            }
//        }
//        titleField.addTextChangedListener(titleWatcher)
//
//        solvedCheckBox.apply {
//            setOnCheckedChangeListener { _, isChecked ->
//                crime.isSolved = isChecked
//            }
//        }
//    }
//}