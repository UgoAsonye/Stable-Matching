/*
 * SMInstance: A class for stable matching mnstances.
 *
 * The class stores two sets of Agents: residents and hospitals. Each
 * resident holds preferences in the form of a ranking of the
 * hospitals, and symmetrically, each hospitals ranks the
 * residents. The main functionalities are: (1) to compute a stable
 * matching, and (2) determine if a given matching is stable.
 * Idea 2?: Maybe you need to loop through the arraylist of residents? For each one, you call the proposal method.
 * 			After the proposal method, the hopsital has to either call the refuse method, or not?
 * Question: Where does the prefers method come in? I think the prefers method is used for the residents?
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SMInstance {
    ArrayList<Agent> hospitals;
    ArrayList<Agent> residents;

    public SMInstance () {
	hospitals = new ArrayList<Agent>();
	residents = new ArrayList<Agent>();
    }


    public SMInstance (String filename) {
	this();
	setInstanceFromFile(filename);
    }

    /*
     * Method: void setInstanceFromFile (String filename)
     * 
     * Sets lists of residents and hospitals from filename. The file
     * is assumed to have the following format:
     *   """
     *   hospitals
     *   <list of hospital names separated by " ">
     *   residents
     *   <list of resident names separated by " ">
     *   preferences
     *   agentName: <ranked list of agentName's preferred matches,
     *     sorted in descending order of preference separated by " ">
     *   agentName: <...>
     *   ...
     *   """
     */
    public void setInstanceFromFile (String filename) {
	try {
	    File file = new File(filename);
	    Scanner scanner = new Scanner(file);	    

	    // set lists of hospital and resident names
	    String name = scanner.nextLine();
	    if (name.equals("hospitals")) {
		populate(hospitals, scanner.nextLine());
	    }
	    else if (name.equals("residents")) {
		populate(residents, scanner.nextLine());
	    }

	    name = scanner.nextLine();
	    if (name.equals("hospitals")) {
		populate(hospitals, scanner.nextLine());
	    }
	    else if (name.equals("residents")) {
		populate(residents, scanner.nextLine());
	    }

	    // read "preferences" line
	    scanner.nextLine();
	    
	    readPreferences(scanner);


	}
	catch (FileNotFoundException e) {
	    System.out.println("File \"" + filename + "\" not found." + e);
	}

    }

    // create Agents with names listed in nameString separated by " "
    private void populate (ArrayList<Agent> agents, String nameString) {
	String[] names = nameString.split(" ");
	for (String name : names) {
	    agents.add(new Agent(name));
	}
    }

    // read and set preferences of an Agent stored in the next line of
    // scanner
    // assumes the following format:
    //   "agentName: firstChoice secondChoice ..."
    private void readPreferences (Scanner scanner) {
	while (scanner.hasNext()) {	    
	    String[] names = scanner.nextLine().split(" ");
	    Agent a = getAgentByName(names[0]);

	    for (int i = 1; i < names.length; i++) {
		a.appendToPrefList(getAgentByName(names[i]));
	    }
	}
    }

    // given the name of an Agent, return a referent to the Agent with
    // this name stored in the current SMInstance (or null if no such
    // Agent is found)
    public Agent getAgentByName (String name) {
	// remove all non-alphanumeric characters from name
	String cleanName = name.replaceAll("[^a-zA-Z0-9]", "");

	for (Agent a : hospitals) {
	    if (a.getName().equals(cleanName))
		return a;
	}

	for (Agent a : residents) {
	    if (a.getName().equals(cleanName))
		return a;
	}

	System.out.println("Agent \"" + cleanName + "\" not found");
	return null;
    }

    // return a string representation of the SMInstance
    public String toString() {
	StringBuilder str = new StringBuilder();
	str.append("hospitals\n");
	
	for (Agent a : hospitals) {
	    str.append(a.getName() + " ");
	}
	
	str.append("\nresidents\n");
	for (Agent a : residents) {
	    str.append(a.getName() + " ");
	}
	
	str.append("\npreferences\n");
	
	for (Agent a : hospitals) {
	    str.append(a.getName() + ": ");

	    for (Agent b : a.getPrefList()) {
		str.append(b.getName() + " ");
	    }

	    str.append("\n");
	}

	for (Agent a : residents) {
	    str.append(a.getName() + ": ");

	    for (Agent b : a.getPrefList()) {
		str.append(b.getName() + " ");
	    }

	    str.append("\n");
	}

	return str.toString();
    }

    // reset the matching in this SMInstance
    // after calling resetMatching() each Agent's curMatch is null and
    // curIndex is -1
    public void resetMatching () {
	for (Agent a : residents)
	    a.reset();

	for (Agent a : hospitals)
	    a.reset();
    }
//NOTE: Test agent before doing SMInstance. Run the Agent Tester as is since they're done.
	//use print methods in proposal and refusal. That would help with knowing what's going on. Print this + was refused for refusal
	//
    
    /*
     * Method: void computeStableMatching () 
     *
     * Applies McVitie and Wilson's algorithm to compute a stable
     * matching. After applying this procedure, the set of
     * resident-hospital pairs (res, hos) with res and hos each
     * others' curMatch forms a stable matching.
     *
     * Note that in the McVitie and Wilson algorithm, residents
     * "arrive" in some order, and each resident should propose to its
     * most favored hospital (and the hospital reacts in response to
     * this proposal).
     */
    public void computeStableMatching () {
    	//complete
		/*
		*McVitie and Wilson algorith:
		* 1. Everyone meets in central location.
		* 2. Residents arrive in arbitrary order
		* 3. 1st resident to arrive proposes to their top choice (since their choice has no offers, they're provisionally matched
		*  		Final decision deferred for later
		* 4. 2nd resident proposes. If different than 1st, same provisional matching. If same, choice needs to decide between them
		* 		Choice decides who to be provisionally matched with and who to reject
		* 		NOTE: This means they send a rejection to the one they don't want
		* 5. Upon rejection, an agent proposes to their next top choice.
		* if refused from all choices, they aren't matched
		* Any proposals and refusals need to be solved before the next agent proposes.
		*
		* Would a for loop be used here or recursion like mentioned in the stable marriage pdf?
		* Perhaps it would loop through the residents. 1st resident proposes, provision, 2nd proposes, provision or
		* choosing between 1st and second, and so on until done.
		 */
		denied = new refusal();
		for (int i = 0; i < residents.size(); i++){
			//how do I make them propose?
			//if I call the refusal method, it will handle the proposals too
			residents.denied();

		}
	
    }

    /*
     * Method: Matching getMatching ()
     * 
     * Creates a new Matching consisting of all hospital-resident
     * pairs that are currently matched. That is, the Matching should
     * contain a pair (res, hos) for each resident res and hospital
     * hos such that hos is res's curMatch and res is hos's
     * curMatch. The Matching should not contain any Pairs where one of
     * the terms is null.
     */
    public Matching getMatching () {

	//we have array list of residents and hospitals
		//how do we make the Matching contain a pair? how do we set the hos and res to be each other's current match?
		//if statement for if any Pair has null?
		//look at every resident and find out who their match is. If they're matched, make a pair of that resident and their match
		//then add that to matching
		//add pair method?
		for (int i = 0; i < residents.size(); i++){
			//for loop that would check each resident
			if (this.getMatching() = true){
				//if statement that would see if the resident is matched. IF they are, then they get a new pair.
				new Pair ();
			}
		}

    }

    /*
     * Method: void setMatching (Matching matching)
     *
     * Iterates over each Pair<Agent> in matching and assigns each
     * agent's curMatch acording to the Pair. That is, for each Pair p
     * in matching, the first Agent in p should be matched with p's
     * second Agent. Similarly, p's second Agent should be matched
     * with p's first Agent.
     */
    public void setMatching (Matching matching) {

	// I'm assuming this would use a for loop to go through each pair in matching
		//how to assign the curMatch according to the pair?
		//Unless it's for loop and then someoe 1st agent in p is matched with p's second agent and p's second agent is matched with p's first
  		//loop through pairs in matching and set the curMatch. ex. set r's match to h and h's ot r.
		//
    }

    /*
     * Method: Pair<Agent> getBlockingPair () 
     * 
     * Iterates over all residents and hospitals in each resident's
     * prefList to find a blocking pair. If a blocking pair is found,
     * it is returned. If no blocking pair is found, null is returned.
     *
     * Recall that a resident res and hospital hos form a *blocking
     * pair* if res prefers hos to res's current match, and hos
     * prefers res to hos's current match.
     */

    public Pair<Agent> getBlockingPair () {

	//use a for loop to go through the residents and hospitals in each resident's prefList arraylist to find a blocking pair.
			//put a for loop in the loop that loops through the prefList of each resident.
		//if found, (if statement?) return that blocking pair. How do we return the specific pair?
		//create a new pair and then return that pair (blocking pair)
		// Would it be like returning it's location in the list?


	return null;
    }

    /*
     * Method: boolean isStable()
     *
     * Returns true if the current matching (i.e., the matching formed
     * by assigning each resident in residents their curMatch
     * hospital) is stable. Recall that a matching is *stable*
     * precisely when it contains no blocking pairs.
     */
    public boolean isStable () {

	//this will need an if statement for if the current matching is stable
		//this relies on blockingpair being done.

	return false; // delete this line
    }

        
}
