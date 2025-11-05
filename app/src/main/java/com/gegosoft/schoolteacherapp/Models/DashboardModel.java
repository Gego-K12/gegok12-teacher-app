package com.gegosoft.schoolteacherapp.Models;

public class DashboardModel {
    String GridItemName;
    int GridItemImage;

    public DashboardModel(String gridItemName, int gridItemImage) {
        GridItemName = gridItemName;
        GridItemImage = gridItemImage;
    }

    public String getGridItemName() {
        return GridItemName;
    }

    public void setGridItemName(String gridItemName) {
        GridItemName = gridItemName;
    }

    public int getGridItemImage() {
        return GridItemImage;
    }

    public void setGridItemImage(int gridItemImage) {
        GridItemImage = gridItemImage;
    }
}
