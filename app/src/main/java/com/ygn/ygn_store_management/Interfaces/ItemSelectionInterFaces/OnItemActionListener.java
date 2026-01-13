package com.ygn.ygn_store_management.Interfaces.ItemSelectionInterFaces;

import com.ygn.ygn_store_management.Models.Dtos.ItemSelectionDto;

public interface OnItemActionListener {
    void onEdit(ItemSelectionDto item, int position);
    void onDelete(ItemSelectionDto item, int position);
}
