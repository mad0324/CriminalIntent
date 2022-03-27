package edu.vt.cs.cs5254.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.vt.cs.cs5254.criminalintent.databinding.FragmentCrimeBinding
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
const val REQUEST_KEY = "request_key"
const val ARG_NEW_DATE = "new_date"

class CrimeFragment : Fragment() {
    private var _binding: FragmentCrimeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        viewModel.loadCrime(crimeId)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrimeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.crimeDate.apply {
            text = viewModel.crime.date.toString()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    viewModel.crime = crime
                    updateUI()
                }
            })
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

        binding.crimeDate.setOnClickListener {
            DatePickerFragment.newInstance(viewModel.crime.date, REQUEST_KEY)
                .show(parentFragmentManager, REQUEST_KEY)
        }
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner)
        { _, bundle ->
            viewModel.crime.date = bundle.getSerializable(ARG_NEW_DATE) as Date
            updateUI()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCrime(viewModel.crime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI() {
        binding.crimeTitle.setText(viewModel.crime.title)
        binding.crimeDate.text = viewModel.crime.date.toString()
        binding.crimeSolved.isChecked = viewModel.crime.isSolved
        binding.crimeSolved.apply {
            isChecked = viewModel.crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}