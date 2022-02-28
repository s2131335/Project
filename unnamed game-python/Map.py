from distutils.command.build import build
from typing_extensions import Self
from webbrowser import get
from Cell import Cell


class Map:
    def __init__(self, rows, cols):
        self._rows = rows
        self._cols = cols
        self._cells = [[Cell() for x in range(cols)] for y in range(rows)]
    
    #TODO: rows getter
    @property
    def rows(self):
        return self._rows

    #TODO: cols getter
    @property
    def cols(self):
        return self._cols
        
    def get_cell(self, row, col):
        # TODO: check whether the position is out of boundary 
        #       if not, return the cell at (row, col)
        #       return None otherwise 
        if row > self._rows-1 or col > self._cols-1 or row < 0 or col < 0:
            print("\033[1;31;46mNext move is out of boundary!\033[0;0m")
            return None 
        else:
            return self._cells[row][col]
        # END TODO 

    def build_cell(self, row, col, cell):
        # TODO: check whether the position is out of boundary 
        #       if not, add a cell (row, col) and return True
        #       return False otherwise 
        if row > self._rows or col > self._cols: 
            print("\033[1;31;46mThe position (%d, %d) is out of boundary!\033[0;0m" %(row, col))
            return False 
        else:
            self._cells[row][col] = cell 
            return True
        # END TODO

    # return a list of cells which are neighbours of cell (row, col) 
    def get_neighbours(self, row, col):
        return_cells = []
        # TODO: return a list of neighboring cells of cell (row, col)
        for x in range(-1,2): 
            for y in range(-1,2): 
                r = row + y
                c = col + x
                if (r >= 0 and c >= 0) and (r < self._rows and c < self._cols) and not (r == row and c == col):
                    return_cells.append(self._cells[r][c])
        return return_cells
        # END TODO
        x

    def display(self):
        # TODO: print the map by calling diplay of each cell 
        content = "   "
        for x in range(0,self._cols):
            content += str(x) + "     "
        print(content)
        for i in range(0,self._rows):
            print(str(i),end=" ")
            for j in range(0,self._cols):
                self._cells[i][j].display()
                if j == 9:
                    print("\n")
        # END TODO
