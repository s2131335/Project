import sys 

if __name__ == '__main__':
	if len(sys.argv) > 1:
		if sys.argv[1] == "basic":
			from Engine import Engine
			eng = Engine('input/map-basic.txt')
			if eng != None:
				eng.run()
		elif sys.argv[1] == "extension":
			from NewEngine import NewEngine
			eng = NewEngine('input/map-extension.txt')
			if eng != None:
				eng.run()
	else:
		print("usage: python3 StrangePlaent.py [basic/extension]")