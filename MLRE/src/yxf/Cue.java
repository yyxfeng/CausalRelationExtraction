/**
 * coreCue store the TreeGraphNode format cue maybe null for conj
 * string completeString stands for the complete cue string
 * 
 * cause is a list of the potential cause TreeGraphNode 
 * effect is the same as cause but the direction can not be sure in this structure 
 * causeNode and effectNode are semantic arguments 
 * causeString and effectString are the final string 
 */
package yxf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;

public class Cue {
	int type=0;
	List<String> coreString = new ArrayList<String>();
	String completeString;
	String causeString;
	String effectString;
	
	List<Tree> causeNode;
	List<Tree> effectNode;
	Tree cueNode;
	Set<TreeGraphNode> cause ;
	Set<TreeGraphNode> effect;
	List<TreeGraphNode> coreCue;
	public Cue(){
		 
		 causeString ="";
		 effectString ="";
		 cause = new HashSet<TreeGraphNode>();
		 effect= new HashSet<TreeGraphNode>();
		 coreCue= new ArrayList<TreeGraphNode>();
		 causeNode = new ArrayList<Tree>();
		 effectNode = new ArrayList<Tree>();
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getCoreString() {
		return coreString;
	}

	public void setCoreString(List<String> coreString) {
		this.coreString = coreString;
	}

	public String getCompleteString() {
		return completeString;
	}

	public void setCompleteString(String completeString) {
		this.completeString = completeString;
	}

	public String getCauseString() {
		return causeString;
	}

	public void setCauseString(String causeString) {
		this.causeString = causeString;
	}

	public String getEffectString() {
		return effectString;
	}

	public void setEffectString(String effectString) {
		this.effectString = effectString;
	}

	public List<Tree> getCauseNode() {
		return causeNode;
	}

	public void setCauseNode(List<Tree> causeNode) {
		this.causeNode = causeNode;
	}

	public List<Tree> getEffectNode() {
		return effectNode;
	}

	public void setEffectNode(List<Tree> effectNode) {
		this.effectNode = effectNode;
	}

	public Tree getCueNode() {
		return cueNode;
	}

	public void setCueNode(Tree cueNode) {
		this.cueNode = cueNode;
	}

	public Set<TreeGraphNode> getCause() {
		return cause;
	}

	public void setCause(Set<TreeGraphNode> cause) {
		this.cause = cause;
	}

	public Set<TreeGraphNode> getEffect() {
		return effect;
	}

	public void setEffect(Set<TreeGraphNode> effect) {
		this.effect = effect;
	}

	public List<TreeGraphNode> getCoreCue() {
		return coreCue;
	}

	public void setCoreCue(List<TreeGraphNode> coreCue) {
		this.coreCue = coreCue;
	}
	
	
	public String getCompletestring() {
		return completeString;
	}
	public void setCompletestring(String completestring) {
		this.completeString = completestring;
	}
	public String getCausestring() {
		return causeString;
	}
	public void setCausestring(String causestring) {
		this.causeString = causestring;
	}
	public String getEffectstring() {
		return effectString;
	}
	public void setEffectstring(String effectstring) {
		this.effectString = effectstring;
	}
	public void print(BufferedWriter f) throws IOException{
		
		
		f.write("{"+causeString+"}");
		f.write("{"+coreCue.get(0).value()+"}");
		f.write("{"+effectString+"}");
		f.newLine();
		f.flush();
		
	}
	

}
