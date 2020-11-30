package com.vitaz.goldfish.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitaz.goldfish.R
import com.vitaz.goldfish.data.viewmodel.ToDoViewModel
import com.vitaz.goldfish.databinding.FragmentListBinding
import com.vitaz.goldfish.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //Setup RecyclerView
        setupRecyclerView()

        //Observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //Set Menu
        setHasOptionsMenu(true)


        return binding.root
    }

    private fun setupRecyclerView() {
        //apply recycler view
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    // Link list_fragment_menu.xml to the actual fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show AlertDialog to confirm removal of all items from Database Table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully Removed Everything!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ ->}
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}