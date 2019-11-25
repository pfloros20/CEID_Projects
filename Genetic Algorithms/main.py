import numpy as np
import math
import random
import copy

courses = []
course = []
professors = []
lectures = []
classes = 4
days = 5
couple_hours = 6

class Course:
    def __init__(self, course_id, hours, dep, prof_id):
        self.course_id = course_id
        self.hours = hours
        self.dep = dep
        self.prof_id = prof_id
        
    #Operator overload for sorting, used later by cross function
    def __gt__(self, other): 
        if(self.course_id>other.course_id): 
            return True
        else: 
            return False
        
class Individual:
    fitness = 0
    def __init__(self):
        #Initialize timetable with -1
        self.timetable = np.empty([classes, couple_hours, days])
        for i in range(classes):
            for j in range(couple_hours):         
                for k in range(days):
                    self.timetable[i][j][k] = -1
                    
    #Searches the whole table for all occurances of value and returns all the positions in which it was found
    def search_timetable(self, value):
        pos = []
        for i in range(classes):
            for j in range(couple_hours):         
                for k in range(days):
                    if self.timetable[i][j][k] == value:
                        pos.append([i, j, k])
        return pos
    #Function to initialize population aka generation 0
    def initialize(self):
        for lec in lectures:
            #Check if random spot is empty and there is no intersection between the lists of professors by course
            #that is to be inserted and courses in other classes at the same time
            #note: this code is reused and will be referenced in comments as legality checker
            while True:
                conflict = False
                i = random.randint(0, classes-1)
                j = random.randint(0, couple_hours-1)
                k = random.randint(0, days-1)
                if self.timetable[i][j][k] == -1:
                    for room in range(classes):
                        if self.timetable[room][j][k] != -1:
                            intersection = list(set(courses[int(self.timetable[room][j][k])].prof_id) & set(lec.prof_id))
                            if intersection != []:
                                conflict = True
                                break
                    if not conflict:
                        self.timetable[i][j][k] = lec.course_id
                        break


    def evaluate_fitness(self):
        self.fitness = 0
        #flags Θ, Λ, Υ 
        dep = [0, 0, 0]
        #for all course done at a given time in different, check the department and raise the appropriate value
        #in the vector, if its neither 0 or 1, same department courses are taught at the same time, reduce fitness
        for j in range(couple_hours):         
            for k in range(days):              
                dep = [0, 0, 0]
                for i in range(classes):
                    if self.timetable[i][j][k] != -1:
                        curr = int(self.timetable[i][j][k])
                        if courses[curr].dep == "Θ":
                            dep[0] += 1
                        elif courses[curr].dep == "Λ":
                            dep[1] += 1
                        elif courses[curr].dep == "Υ":
                            dep[2] += 1
                for i in dep:
                    if i != 1 and i != 0:
                        self.fitness -= 2

        #for each class on a specific day save the position of the courses in a vector, if subsequent differences are 1
        #it means the courses are taught with no empty space in between, increase fitness
        for i in range(classes):
            for k in range(days):
                pos = []
                for j in range(couple_hours):
                    if self.timetable[i][j][k] != -1:
                        pos.append(j)
                for c in range(1, len(pos)):
                    diff = pos[c] - pos[c-1]
                    if diff == 1:
                        self.fitness += 25

        #list that saves all the courses that are taught in more than 2 hours
        doubles = []
        for c in courses:
            if c.hours > 2:
                doubles.append(c)
                
        #for each such course save position of its seperate double_hours, if its difference is not one, its either
        #taught on the same day or with 1 or more days in between, increase fitness
        for c in doubles:
            pos = []
            for i in range(classes):
                for j in range(couple_hours):         
                    for k in range(days):
                        if c.course_id == self.timetable[i][j][k]:
                            pos.append(k)
                            
            if abs(pos[1] - pos[0]) != 1:
                self.fitness += 10
    
        return self.fitness
        
class Population:
    fitest = None
    fitness = 0
    individuals = []
    #Initialize population on creation of population object
    def __init__(self, n, cross_prob = 0, mutate_prob = 0):
        self.n = n
        self.cross_prob = cross_prob
        self.mutate_prob = mutate_prob
        for i in range(n):
            ind = Individual()
            ind.initialize()
            self.individuals.append(ind)
            
    #evaluate population fitness according to the sum of the individual fitnesses 
    #and keep the fitest indicidual of the population
    def evaluate_fitness(self):
        self.fitness = 0
        self.fitest = copy.deepcopy(self.individuals[0])
        for i in range(self.n):
            ind_fitness = self.individuals[i].evaluate_fitness()
            self.fitness += ind_fitness
            if self.fitest.fitness < ind_fitness:
                self.fitest = copy.deepcopy(self.individuals[i])

    #select individuals of the population accourding to slotted roulette wheel
    def select(self):
        self.evaluate_fitness()
        prob = []
        #calculate individual probabilities according to fitness
        for i in range(self.n):
            prob.append(self.individuals[i].fitness/self.fitness)
        #calculate cumulative probabilities
        for i in range(1, self.n):
            prob[i] += prob[i-1]
        
        #selected keeps the indices of selected individuals from the population
        selected = []
        self.fitness = 0
        for i in range(self.n):
            spin = random.random()
            for j in range(self.n):
                if spin < prob[j]:
                    selected.append(j)
                    self.fitness += self.individuals[j].fitness
                    break 

        T = [self.individuals[i] for i in selected]
        #new population has the selected individuals
        self.individuals  = T

        
    def cross(self):
        #select individuals to cross according to cross probability
        selected = []
        for i in range(self.n):
            p = random.random()
            if p < self.cross_prob:
                selected.append(i)
        #make sure they can be divided in pairs
        if len(selected)%2 != 0:
            selected.pop()
        
        for i in range(0, int(len(selected)/2), 2):
            #use cross point on list of leactures
            cross_point = random.randint(0, len(lectures)-1)
            #get two parts of the list
            first = lectures[0:cross_point]
            second = lectures[cross_point:]
            #make the new individuals that will replace the old ones
            first_child = Individual()
            second_child = Individual()
            #lists to save positions of lectures to be inserted on children
            first_pos = []
            second_pos = []
            
            #make sure leactures with the same id are not divided since they cant be distinguised
            if cross_point != 0:
                if first[cross_point-1].course_id == second[0].course_id:
                    second.insert(0, first[cross_point-1])
                    first.pop()
                
            #get positions of lectures from parents
            for lec in sorted(set(first)):
                search = self.individuals[i].search_timetable(lec.course_id)
                for result in search:
                    first_pos.append(result)
            for lec in sorted(set(second)):
                search = self.individuals[i+1].search_timetable(lec.course_id)
                for result in search:
                    second_pos.append(result)
                
            #insert the lectures from first parent
            for j in range(len(first_pos)):
                [pi, pj, pk] = first_pos[j]
                first_child.timetable[pi][pj][pk] = first[j].course_id
                
            #insert the lectures from second parent
            for j in range(len(second_pos)):
                [pi, pj, pk] = second_pos[j]
                #legality checker
                while True:
                    conflict = False
                    if first_child.timetable[pi][pj][pk] == -1:
                        for room in range(classes):
                            if first_child.timetable[room][pj][pk] != -1:
                                intersection = list(set(courses[int(first_child.timetable[room][pj][pk])].prof_id) & set(second[j].prof_id))
                                if intersection != []:
                                    conflict = True
                                    break
                        if not conflict:
                            first_child.timetable[pi][pj][pk] = second[j].course_id
                            break
                    pi = random.randint(0, classes-1)
                    pj = random.randint(0, couple_hours-1)
                    pk = random.randint(0, days-1)
            
            #clear lists of positions
            first_pos.clear()
            second_pos.clear()
            
            #get positions of lectures from parents
            for lec in sorted(set(first)):
                search = self.individuals[i+1].search_timetable(lec.course_id)
                for result in search:
                    first_pos.append(result)
            for lec in sorted(set(second)):
                search = self.individuals[i].search_timetable(lec.course_id)
                for result in search:
                    second_pos.append(result)
                
            #insert the lectures from first parent
            for j in range(len(first_pos)):
                [pi, pj, pk] = first_pos[j]
                second_child.timetable[pi][pj][pk] = first[j].course_id
                
            #insert the lectures from second parent
            for j in range(len(second_pos)):
                [pi, pj, pk] = second_pos[j]
                #legality check
                while True:
                    conflict = False
                    if second_child.timetable[pi][pj][pk] == -1:
                        for room in range(classes):
                            if first_child.timetable[room][pj][pk] != -1:
                                intersection = list(set(courses[int(second_child.timetable[room][pj][pk])].prof_id) & set(second[j].prof_id))
                                if intersection != []:
                                    conflict = True
                                    break
                        if not conflict:
                            second_child.timetable[pi][pj][pk] = second[j].course_id
                            break
                    pi = random.randint(0, classes-1)
                    pj = random.randint(0, couple_hours-1)
                    pk = random.randint(0, days-1)

            #replace parents with children
            self.individuals[i] = first_child
            self.individuals[i+1] = second_child
                        
    def mutate(self):
        #for each lecture in table if it hits the chance to mutate, change its position
        for i in range(self.n):            
            for pi in range(classes):
                for pj in range(couple_hours):         
                    for pk in range(days):
                        if self.individuals[i].timetable[pi][pj][pk] != -1:
                            p = random.random()
                            if p < self.mutate_prob:
                                gene = int(self.individuals[i].timetable[pi][pj][pk])
                                #legality check
                                while True:
                                    conflict = False
                                    ri = random.randint(0, classes-1)
                                    rj = random.randint(0, couple_hours-1)
                                    rk = random.randint(0, days-1)
                                    if self.individuals[i].timetable[ri][rj][rk] == -1:
                                        for room in range(classes):
                                            if self.individuals[i].timetable[room][rj][rk] != -1:
                                                intersection = list(set(courses[int(self.individuals[i].timetable[room][rj][rk])].prof_id) & set(courses[gene].prof_id))
                                                if intersection != []:
                                                    conflict = True
                                                    break
                                        if not conflict:
                                            self.individuals[i].timetable[ri][rj][rk] = gene
                                            self.individuals[i].timetable[pi][pj][pk] = -1
                                            break
        
        #Elitism
        #kick the last one and replace with fitest
        self.individuals.pop()
        self.individuals.append(self.fitest)
                        
#helper print function
def print_pop(pop):
    print("Pop Fitness: "+str(pop.fitness))
    for i in range(pop.n):
        print(str(i)+" Fitness: "+str(pop.individuals[i].fitness))
        print(pop.individuals[i].timetable)
    print()
                
def main():
    #save data for courses, professors and lectures
    professors.append("Κοσμαδάκης Σταύρος")
    professors.append("Κακλαμάνης Χρήστος")
    professors.append("Ζαρολιάγκης Χρήστος")
    professors.append("Λυκοθανάσης Σπυρίδων")
    professors.append("Νικολετσέας Σωτήριος")
    professors.append("Μπούρας Χρήστος")
    professors.append("Μεγαλοοικονόμου Βασίλειος")
    professors.append("Χατζηλυγερούδης Ιωάννης")
    professors.append("Μακρής Χρήστος")
    professors.append("Ξένος Μιχάλης")
    professors.append("Παυλίδης Γεώργιος")
    professors.append("Νανόπουλος Φώτιος")
    professors.append("Γαροφαλάκης Ιωάννης")
    professors.append("Βέργος Χαρίδημος")
    professors.append("Μπερμπερίδης Κωνσταντίνος")
    professors.append("Νικολός Δημήτριος")
    professors.append("Σκλάβος Νικόλαος")

    course.append("Μαθηματική Λογική & Εφαρμογές της")
    course.append("Παράλληλοι Αλγόριθμοι")
    course.append("Κρυπτογραφία")
    course.append("Υπολογιστική Νoημοσύνη")
    course.append("Σημασιολογία & Ορθότητα Προγραμμάτων")
    course.append("Τεχνολογίες Υλοποίησης Αλγορίθμων")
    course.append("Ευρυζωνικές Τεχνολογίες")
    course.append("Τηλεματική & Νέες Υπηρεσίες")
    course.append("Βάσεις Δεδομένων 2")
    course.append("Τεχνολογίες Ευφυών Συστημάτων & Ρομποτική")
    course.append("Εξόρυξη Δεδομένων και Αλγόριθμοι Μάθησης")
    course.append("Εφαρμοσμένα Πληροφοριακά Συστήματα 2")
    course.append("Εισαγωγή στην Βιοπληροφορική")
    course.append("Υπολογιστικές Μέθοδοι στην Οικονομία")
    course.append("e-Επιχειρείν")
    course.append("Σχεδίαση Συστημάτων με χρήση Υπολογιστών")
    course.append("Ψηφιακή Επεξεργασία & Ανάλυση Εικόνας")
    course.append("Ασύρματες & Κινητές Επικοινωνίες")
    course.append("Ειδικά Θέματα Σχεδίασης Ψηφιακών Συστημάτων")
    course.append("Σχεδιασμός Συστημάτων VLSI")
    
    courses.append(Course(0, 4, "Θ", [0] ))
    courses.append(Course(1, 4, "Θ", [1, 2] ))
    courses.append(Course(2, 2, "Θ", [1] ))
    courses.append(Course(3, 4, "Θ", [3]))
    courses.append(Course(4, 2, "Θ", [0, 4]))
    courses.append(Course(5, 4, "Θ", [2]))
    courses.append(Course(6, 2, "Θ", [5]))
    courses.append(Course(7, 2, "Θ", [5]))
    courses.append(Course(8, 4, "Λ", [6]))
    courses.append(Course(9, 2, "Λ", [7]))
    courses.append(Course(10, 4, "Λ", [8, 6]))
    courses.append(Course(11, 2, "Λ", [9, 10, 11]))
    courses.append(Course(12, 4, "Λ", [8, 3]))
    courses.append(Course(13, 2, "Λ", [10]))
    courses.append(Course(14, 2, "Λ", [12]))
    courses.append(Course(15, 2, "Υ", [13]))
    courses.append(Course(16, 4, "Υ", [14]))
    courses.append(Course(17, 2, "Υ", [14]))
    courses.append(Course(18, 4, "Υ", [15]))
    courses.append(Course(19, 2, "Υ", [16]))
        
    for c in courses:
        lectures.append(c)
        if c.hours > 2:
            lectures.append(c)
        
    
    parameters = [ [20, 0.6, 0.00], [20, 0.6, 0.01], [20, 0.6, 0.1], [20, 0.6, 0.5], [20, 0.9, 0.01], [20, 0.9, 0.1], [20, 0.1, 0.1],
                 [100, 0.6, 0.01], [100, 0.6, 0.1], [100, 0.6, 0.5], [100, 0.1, 0.1], [100, 0.9, 0.1] ]

    early_stop = 30
    early_stop_c = 0
    
    results = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    gens = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    
    #data to be saved for the creation of the charts for the report
    graph_data = [[0 for i in range(len(parameters))] for j in range(100)]
    
    #run 10 times to get averages
    for j in range(10):
        print(j)
        #run for each set of parameters
        for p in range(len(parameters)):
            print(parameters[p])
            pop = Population(parameters[p][0], parameters[p][1], parameters[p][2])
            pop.evaluate_fitness()
            print("Initial Fitest: "+str(pop.fitest.fitness)+" in gen: 0")

            fitest = pop.fitest
            early_stop_c = 0
            gen = 0
            #run for maximum of generations from gen 1, gen 0 is initial
            for i in range(1,101):
                pop.select()
                pop.cross()
                pop.mutate()
                pop.evaluate_fitness()

                #save the fitest individual of the population along with the generation
                if pop.fitest.fitness > fitest.fitness:
                    fitest = pop.fitest
                    gen = i
                    early_stop_c = 0
                else:
                    early_stop_c +=1

                #stop early if there hasnt been improvement for early_stop generations
                if early_stop_c == early_stop:
                    break
                #save sum of fitest from each generation
                graph_data[i-1][p] += pop.fitest.fitness
#             print(fitest.timetable)
            print("Best fitest: "+str(fitest.fitness)+" in gen: "+str(gen))
            print()
            #save results for report table
            results[p] += fitest.fitness
            gens[p] += i
            
    #get averages
    for i in range(12):
        results[i] /= 10
        gens[i] /= 10
        
    for i in range(100):
        for j in range(len(parameters)):
            graph_data[i][j] /= 10
    #save graph data to file
    file = open("data.xls", "a")
    
    for i in range(100):
        file.write(str(i+1)+";")
        for j in range(len(parameters)):
            if j <len(parameters)-1:
                file.write(str(graph_data[i][j])+";")
            else:
                file.write(str(graph_data[i][j]))
        file.write("\n")
    #print results
    print(results)
    print(gens)

main()

  