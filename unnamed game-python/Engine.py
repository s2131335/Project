from distutils.command.build import build
from Map import Map
from Cell import Plain, Mountain, Swamp
from GameCharacter import Player, Goblin

class Engine:
    def __init__(self, data_file):
        self._actors = []
        self._remove = [] 
        self._map = None 
        self._player = None 
        with open(data_file, "r") as fp:
            line = fp.readline()
            if not line:
                return None 
            else:
                items = line.split()
                if len(items) != 5:
                    print("INVALID DATA FILE.")
                    return None 
                num_of_row = int(items[0])
                num_of_col = int(items[1])
                p_ox = int(items[2])
                p_hp = int(items[3])
                num_of_goblins = int(items[4])

            self._map = Map(num_of_row, num_of_col)
            
            # TODO: initialize each cell of the map object 
            #       using the build_cell method 
            for i in range(0,num_of_row):
                terrain = fp.readline().split()
                for j in range(0,num_of_col):
                    if terrain[j] == 'P':
                        self._map.build_cell(i,j,Plain(i,j))
                    if terrain[j] == 'S':
                        self._map.build_cell(i,j,Swamp(i,j))
                    if terrain[j] == 'M':
                        self._map.build_cell(i,j,Mountain(i,j))
                
            
            # END TODO
            self._player = Player(num_of_row - 1, 0, p_hp, p_ox)
            # TODO: initilize the position of the player 
            #       using the set_occupant and occupying setter
            #       add the player to _actors array 
            init_cell = self._map.get_cell(num_of_row-1,0)
            init_cell.set_occupant(self._player)
            self._player.occupying = init_cell
            self._actors.append(self._player)
            # END TODO 


            for gno in range(num_of_goblins):
                # TODO: initilize each Goblin on the map
                #       using the set_occupant and occupying setter;
                #       add each Goblin to _actors array 
                goblin_data = fp.readline().split()
                goblin = Goblin(int(goblin_data[0]),int(goblin_data[1]),goblin_data[2: ])
                init_cell = self._map.get_cell(int(goblin_data[0]),int(goblin_data[1]))
                init_cell.set_occupant(goblin)
                goblin.occupying = init_cell
                self._actors.append(goblin)
                # END TOD

    def run(self):
        # main rountine of the game
        self.print_info()
        while not self.state():            
            for obj in self._actors:
                if obj.active: 
                    obj.act(self._map)
            self.print_info()
            self.clean_up()
        self.print_result()

    def clean_up(self):
        # TODO: remove all objects in _actors which is not active 
        clean_list = []
        for i in range (0,len(self._actors)):
            if not self._actors[i].active :
                clean_list.append(i)
        for item in clean_list:
            del self._actors[item]
        # END TODO

    # check if the game ends and return if the player win or not.
    def state(self):
         # TODO: check if the game ends and 
         #      return an integer for the game status 
        if self._player.hp < 1 or self._player.oxygen < 1:
            return -1
        if self._player.row == 0 and self._player.col == (self._map.cols-1):
            return 1

        return 0
        # END TODO 
    def print_info(self):
        self._map.display()
        # TODO: display the remaining oxygen and HP 
        print("Oxygen: %d, HP: %d."%(self._player.oxygen,self._player.hp))
        
        # END TODO 
    def print_result(self):
        # TODO: print a string that shows the result of the game. 
        if self.state() == 1:
            msg = "\033[1;33;41mCongrats! You win!\033[0;0m"
        else:
            msg = "\033[1;33;41mBad Luck! You lose.\033[0;0m"
        print(msg)
         # END TODO
        
