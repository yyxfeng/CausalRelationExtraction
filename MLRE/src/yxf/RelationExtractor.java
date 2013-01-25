/**
 * The algorithm description can be found in  
 */
package yxf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import yxf.Cue;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class RelationExtractor {
	@SuppressWarnings("serial")
	public HashMap<Integer, Cue> getCandidate(String s, LexicalizedParser lp ,Tree parse) {
		Set<String> VerbType = new HashSet<String>() {
			{
				add("VB");
				add("VBD");
				add("VBZ");
				add("VBG");
				add("VBN");
				add("VBP");
			}
		};
		Set<String> NounType = new HashSet<String>() {

			{
				add("NN");
				add("NNS");
				add("NNP");
				add("NNPS");
			}
		};
		Set<String> special = new HashSet<String>() {
			{
				add(" result in ");
				add(" result of ");
				add(" reason of ");
				add(" by reason ");
				add(" reasons of ");
				add(" consequently ");
				add(" consequence of ");
				add(" in consequence ");
				add(" on the ground ");
				add(" on the grounds ");
				add(" on account of ");
				add(" as a consequence ");
				add(" so that ");
				add(" therefore ");
				add(" thus ");
				add(" hence ");
				add(" accordingly ");
				add(" because of ");
				add(" due to ");
				add(" stem from ");
				add(" lead to ");
				add(" leads to ");
				add(" now that ");
				add(" owing to ");
				add(" thanks to ");
				add(" so as to ");
			
			}
		};
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

		
		
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	
		List<TypedDependency> tdlo = gs.typedDependencies(false);

		// Search the candidate
		Set<TreeGraphNode> verbset = new HashSet<TreeGraphNode>();

		HashMap<Integer, Cue> Cuemap = new HashMap<Integer, Cue>();
		Set<Integer> returnsign = new HashSet<Integer>();
		Map<Integer, Integer> indexmapa = new HashMap<Integer, Integer>();
		int indexfor10 = 0;
		for (int i = 0; i < tdlo.size(); i++) {
			int size = verbset.size();
			// 添加 cue 设置基本的属性
			// 专为8设置 cue index
			int a = cuefilter(verbset, tdlo.get(i));
			// -----------------------------------------------------------------------------------------------------------------
			
			returnsign.add(a);

			if (size != verbset.size() && a != 7 && a != 10 && a != 8) {
				Cue b = new Cue();
				b.coreCue.add((tdlo.get(i).gov()));
				b.coreString.add((tdlo.get(i).gov().value()));
				b.setCompletestring(tdlo.get(i).gov().value());
				Cuemap.put(tdlo.get(i).gov().index(), b);
				indexmapa.put(tdlo.get(i).gov().index(), a);

			} else if (size != verbset.size() && ((a == 7) || (a == 10))) {
				Cue b = new Cue();
				b.coreCue.add((tdlo.get(i).dep()));
				b.coreString.add((tdlo.get(i).dep().value()));
				b.setCompletestring(tdlo.get(i).dep().value());
				Cuemap.put(tdlo.get(i).dep().index(), b);
				indexmapa.put(tdlo.get(i).dep().index(), a);

			} else if (size != verbset.size() && a == 8) {
				if (!Cuemap.keySet().contains(tdlo.get(i).gov().index())) {
					Cue b = new Cue();
					b.coreCue.add((tdlo.get(i).gov()));
					b.coreString.add((tdlo.get(i).gov().value()));
					b.setCompletestring(tdlo.get(i).gov().value());
					Cuemap.put(tdlo.get(i).gov().index(), b);
					indexmapa.put(tdlo.get(i).gov().index(), a);
				}
				if (!Cuemap.keySet().contains(tdlo.get(i).dep().index())) {
					Cue b = new Cue();
					b.coreCue.add((tdlo.get(i).dep()));
					b.coreString.add((tdlo.get(i).dep().value()));
					b.setCompletestring(tdlo.get(i).dep().value());
					Cuemap.put(tdlo.get(i).dep().index(), b);
					indexmapa.put(tdlo.get(i).dep().index(), a);
				}
			}

			// ----------------------------------------------------------------------------------------------------------
			// add more informtion
			// 4: dep as key and add cause

			if (a == 4) {
				if (tdlo.get(i).dep().index() < tdlo.get(i).gov().index()) {
					Cuemap.get(tdlo.get(i).dep().index()).effect.add(tdlo
							.get(i).gov());
				} else {
					Cuemap.get(tdlo.get(i).dep().index()).cause.add(tdlo.get(i)
							.gov());
				}

			}
			// 5: gov as key and add cause
			if (a == 5) {
				if (tdlo.get(i).gov().index() < tdlo.get(i).dep().index()) {
					Cuemap.get(tdlo.get(i).gov().index()).effect.add(tdlo
							.get(i).dep());
				} else {
					Cuemap.get(tdlo.get(i).gov().index()).cause.add(tdlo.get(i)
							.dep());
				}
			}
			if (a == 6) {
				if (tdlo.get(i).gov().index() < tdlo.get(i).dep().index()) {
					Cuemap.get(tdlo.get(i).gov().index()).effect.add(tdlo
							.get(i).dep());
				} else {
					Cuemap.get(tdlo.get(i).gov().index()).cause.add(tdlo.get(i)
							.dep());
				}
			}
			if (a == 7) {
				if (tdlo.get(i).dep().index() < tdlo.get(i).gov().index()) {
					Cuemap.get(tdlo.get(i).dep().index()).effect.add(tdlo
							.get(i).gov());
				} else {
					Cuemap.get(tdlo.get(i).dep().index()).cause.add(tdlo.get(i)
							.gov());
				}
			}
			if (a == 8) {

				Cuemap.get(tdlo.get(i).dep().index()).cause.add(tdlo.get(i)
						.gov());

				Cuemap.get(tdlo.get(i).gov().index()).effect.add(tdlo.get(i)
						.dep());

			}
			if (a == 9) {
				if (returnsign.contains(10)) {
					if (indexfor10 < tdlo.get(i).gov().index()) {
						Cuemap.get(indexfor10).effect.add(tdlo.get(i).gov());
					} else {
						Cuemap.get(indexfor10).cause.add(tdlo.get(i).gov());
					}
				} else {

				}
			}

			if (a == 10) {
				if (tdlo.get(i).dep().index() < tdlo.get(i).gov().index()) {
					Cuemap.get(tdlo.get(i).dep().index()).effect.add(tdlo
							.get(i).gov());
				} else {
					Cuemap.get(tdlo.get(i).dep().index()).cause.add(tdlo.get(i)
							.gov());
				}
				if (returnsign.contains(9)) {
					if (tdlo.get(i).dep().index() < tdlo.get(i).gov().index()) {
						Cuemap.get(tdlo.get(i).dep().index()).effect.add(tdlo
								.get(i).gov());
					} else {
						Cuemap.get(tdlo.get(i).dep().index()).cause.add(tdlo
								.get(i).gov());
					}
				}
				indexfor10 = tdlo.get(i).dep().index();
			}
		}

		// advcl advmod xcomp的重新整理
		boolean sign = returnsign.contains(4) & returnsign.contains(9);
		if (sign) {
			List<Integer> delindex = new ArrayList<Integer>();
			List<Integer> advclindex = new ArrayList<Integer>();
			List<Integer> xcompindex = new ArrayList<Integer>();
			for (int j1 = 0; j1 < tdlo.size(); j1++) {
				if (tdlo.get(j1).reln().getShortName()
						.equalsIgnoreCase("advcl")) {
					advclindex.add(j1);
				}
				if (tdlo.get(j1).reln().getShortName()
						.equalsIgnoreCase("xcomp")) {
					xcompindex.add(j1);
				}
			}
			for (int j1 = 0; j1 < xcompindex.size(); j1++) {
				for (int j2 = 0; j2 < advclindex.size(); j2++) {
					if (tdlo.get(xcompindex.get(j1)).dep().index() == tdlo
							.get(advclindex.get(j2)).gov().index()) {
						int resevre = tdlo.get(xcompindex.get(j1)).gov()
								.index();
						int del = tdlo.get(advclindex.get(j2)).gov().index();
						Cuemap = merge(Cuemap, resevre, del);
						delindex.add(del);
					}
				}
			}

			for (int j = 0; j < delindex.size(); j++) {
				Cuemap.remove(delindex.get(j));
			}
		}
		// ----------------------------------------------------------------------------------------------------------
		// merge two close cue
		TreeSet<Integer> ts = new TreeSet<Integer>(Cuemap.keySet());
		Iterator<Integer> iterint = ts.iterator();

		if (iterint.hasNext()) {
			int num1 = iterint.next();
			List<Integer> removelist = new ArrayList<Integer>();

			while (iterint.hasNext()) {

				int num2 = iterint.next();
				if ((num2 - num1) == 1) {

					Cuemap = merge(Cuemap, num1, num2);
					removelist.add(num2);
				}
				num1 = num2;
			}
			for (Integer a1 : removelist) {
				Cuemap.remove(a1);
			}

		}
		// -----------------------------------------------------------------------------------------------------------------
		// completeString 添加
		Iterator<Entry<Integer, Cue>> itercue = Cuemap.entrySet().iterator();
		while (itercue.hasNext()) {

			Entry<Integer, Cue> cue = itercue.next();

			// 重新制定completeString
			int judgetype = 0;
			if (cue.getValue().coreCue.get(0).parent().value()
					.equalsIgnoreCase("IN")
					|| cue.getValue().coreCue.get(0).parent().value()
							.equalsIgnoreCase("TO")) {
				judgetype = 1;
			}

			if (cue.getKey() > 2
					&& cue.getKey() < (parse.taggedYield().size() - 1)) {
				
				String test = parse.taggedYield().get(cue.getKey() - 2).tag();
				String test1 = parse.taggedYield().get(cue.getKey() - 2).word();
				if (judgeforCS(test, test1, judgetype)) {
					cue.getValue().setCompletestring(
							test1 + " " + cue.getValue().completeString);
					test = parse.taggedYield().get(cue.getKey() - 3).tag();
					test1 = parse.taggedYield().get(cue.getKey() - 3).word();
					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								test1 + " " + cue.getValue().completeString);
					}
				}
				test = parse.taggedYield().get(cue.getKey()).tag();
				test1 = parse.taggedYield().get(cue.getKey()).word();

				if (judgeforCS(test, test1, judgetype)) {
					cue.getValue().setCompletestring(
							cue.getValue().completeString + " " + test1);
					test = parse.taggedYield().get(cue.getKey() + 1).tag();
					test1 = parse.taggedYield().get(cue.getKey() + 1).word();

					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								cue.getValue().completeString + " " + test1);
					}
				}

			} else if (cue.getKey() > 2
					&& !(cue.getKey() < (parse.taggedYield().size() - 1))) {
				String test = parse.taggedYield().get(cue.getKey() - 2).tag();
				String test1 = parse.taggedYield().get(cue.getKey() - 2).word();

				if (judgeforCS(test, test1, judgetype)) {
					cue.getValue().setCompletestring(
							test1 + " " + cue.getValue().completeString);
					test = parse.taggedYield().get(cue.getKey() - 3).tag();
					test1 = parse.taggedYield().get(cue.getKey() - 3).word();
					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								test1 + " " + cue.getValue().completeString);
					}
				}
				int key = parse.taggedYield().size() - cue.getKey();
				if (key == 1) {
					test = parse.taggedYield().get(cue.getKey()).tag();
					test1 = parse.taggedYield().get(cue.getKey()).word();

					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								cue.getValue().completeString + " " + test1);

					}
				}

			} else if (!(cue.getKey() > 2)
					&& cue.getKey() < (parse.taggedYield().size() - 1)) {
				String test = parse.taggedYield().get(cue.getKey()).tag();
				String test1 = parse.taggedYield().get(cue.getKey()).word();

				if (judgeforCS(test, test1, judgetype)) {
					cue.getValue().setCompletestring(
							cue.getValue().completeString + " " + test1);
					test = parse.taggedYield().get(cue.getKey() + 1).tag();
					test1 = parse.taggedYield().get(cue.getKey() + 1).word();
					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								cue.getValue().completeString + " " + test1);
					}
				}
				int key = cue.getKey();
				if (key == 2) {
					test = parse.taggedYield().get(cue.getKey() - 2).tag();
					test1 = parse.taggedYield().get(cue.getKey() - 2).word();

					if (judgeforCS(test, test1, judgetype)) {
						cue.getValue().setCompletestring(
								test1 + " " + cue.getValue().completeString);

					}
				}

			}

			for (int j = 0; j < cue.getValue().coreString.size(); j++) {

				if (!cue.getValue().completeString
						.contains(cue.getValue().coreString.get(j))) {
					cue.getValue().setCompletestring(
							cue.getValue().completeString + " "
									+ cue.getValue().coreString.get(j));
				}

			}

		}
		// -----------------------------------------------------------------------------------------------------------------
		
		
		List<String> sl = new ArrayList<String>();
		//sl.add(" ");
		
		for(Tree c : parse.getLeaves()){
			sl.add(c.value());
		}
		
		List<int[]> speciallist = new ArrayList<int[]>();
		String st =" "+s ;
		int start = 0;
		int ps=0;
		String[] co = s.split(" ");
		
		List<Integer> to = new ArrayList<Integer>();
		int j1 = 0;
		for(int i =0;i<co.length;){
			if(j1<sl.size()){
			
				if(co[i].equalsIgnoreCase(sl.get(j1))){
					i++;
					j1++;
				}else{
					to.add(i);
					if(j1<sl.size()-2&& i <co.length-1){
						
					
							if(!(sl.subList(j1, sl.size()).indexOf(co[i+1])==(-1))){
								j1=j1+sl.subList(j1, sl.size()).indexOf(co[i+1]);
								
							}else{
								j1++;
							}
		
					}
					
					i++;
					
				}
			}
			
			
		}
		
		for(String sp:special){
			
			while(st.contains(sp)){
				
				String[] temp = sp.split(" ");
				int[] t = new int[2];
				
				
				t[0] =st.substring(0, st.indexOf(sp)).split(" ").length+ps;
				ps = t[0]+temp.length-1;
				List<Integer> re = new ArrayList<Integer>();
				
				for(Integer f : to){
					if(t[0]>f){
						t[0]++;
						re.add(f);
						
					}
				}
				
				for(Integer a : re){
					to.remove(a);
				}
				
				t[1] = t[0]+temp.length-2;
				
				
				
				start = st.indexOf(sp)+sp.length();
				st= st.substring(start);
				speciallist.add(t);
			}
		}
		
		//确定数据类型！
	
		Set<int[]> used = new HashSet<int[]>();
		for(int key:Cuemap.keySet()){
			int type=0;
			for(int[] t :speciallist){
				
				if(key>=(t[0]+1)&&key<=(t[1]+1)){
					type =1;
					StringBuilder sb = new StringBuilder();
					for(int j= t[0];j<=t[1];j++){
					
						sb.append(sl.get(j-1));
						sb.append(" ");
					}
					
					Cuemap.get(key).completeString=sb.toString().trim();
					used.add(t);
				
					break;
					
				}
			}
			if(type==0){
				for(TreeGraphNode t :Cuemap.get(key).coreCue){
					if(!VerbType.contains(t.parent().value()) && !NounType.contains(t.parent().value()) ){
						type=2;
					}
				}
			}
			if(type==0){
				for(TreeGraphNode t :Cuemap.get(key).coreCue){
					if(VerbType.contains(t.parent().value().toUpperCase())){
						type=3;
					}
				}
			}
			Cuemap.get(key).type=type;
		}
	
		// ---------------------------------------------------------------------------------------------------------
		Set<Integer> removelist = new HashSet<Integer>();
		for (int key : Cuemap.keySet()) {
			boolean check = checksides(Cuemap, key);
			boolean a = true;
			boolean b = true;
			
			if (Cuemap.get(key).coreCue.size() != 0) {
				a = !Cuemap.get(key).coreCue.get(0).parent().value()
						.equalsIgnoreCase("IN");
				b = !Cuemap.get(key).coreCue.get(0).parent().value()
						.equalsIgnoreCase("TO");
			} 
			
			if (check && a && b ) {
				removelist.add(key);
			}
		}

		for (Integer k : removelist) {

			Cuemap.remove(k);
		}
		
	
		
		
		// --------------------------------------------------------------------------------------------------------------------------------------
		// find the whole causeNode and effectNode !!
		Set<String> CauseCheckSet = new HashSet<String>() {

			{
				add("VP");
				add("ADVP");
				add("ADJP");
				add("CONJP");
				add("VB");
				add("VBD");
				add("VBZ");
				add("VBG");
				add("VBN");
				add("VBP");
			}
		};
	
		List<Integer> remolist = new ArrayList<Integer>();
		for (int k : Cuemap.keySet()) {
			Tree root = new TreeGraphNode();
			root = Cuemap.get(k).coreCue.get(0);
			while (!root.value().equalsIgnoreCase("root")) {
				root = root.parent();
			}
			
			
			Iterator<TreeGraphNode> iter1 = Cuemap.get(k).cause.iterator();
			Iterator<TreeGraphNode> iter2 = Cuemap.get(k).effect.iterator();
			TreeGraphNode cause = new TreeGraphNode();
			TreeGraphNode effect = new TreeGraphNode();
			if (Cuemap.get(k).coreCue.size() != 0
					&& (Cuemap.get(k).cause.size() + Cuemap.get(k).effect
							.size()) >= 2) {

				while (iter1.hasNext()) {
					cause = iter1.next();
					List<Tree> corepath = root
							.dominationPath(Cuemap.get(k).coreCue.get(0));
					List<Tree> causepath = root.dominationPath(cause);
					int[] sizerank = { causepath.size(), corepath.size() };
					int sign1 = sizerank[0] < sizerank[1] ? sizerank[0]
							: sizerank[1];
					for (int i = 0; i < sign1; i++) {
						boolean a = corepath.get(i).equals(causepath.get(i));

						if (!a && CauseCheckSet.contains(causepath.get(i).value())) {
							Tree findfirst = causepath.get(i);
							Tree findsecond = new TreeGraphNode();
							boolean find = false;
							for(Tree c :findfirst.parent().parent().children()){
								if(c.value().equalsIgnoreCase("CC")&&findfirst.parent().value().equalsIgnoreCase("VP")){
									break;
								}
							}
							while (!findfirst.value().equalsIgnoreCase("root")) {
						
								if (findfirst.value().equalsIgnoreCase("VP")) {
									for (Tree d : findfirst.parent().children()) {
										if (d.value().equalsIgnoreCase("NP")) {
											findsecond = d;
											find = true;
										}
									}
								}
								if (findfirst.value().equalsIgnoreCase("NP")) {
									for (Tree d : findfirst.parent().children()) {
										if (d.value().equalsIgnoreCase("VP")) {
											findsecond = d;
											find = true;
										}
									}
								}
								if (find) {	break;	}
								findfirst = findfirst.parent();

							}
							if(find){
								if(findfirst.value().equalsIgnoreCase("NP")){
									Cuemap.get(k).causeNode.add(findfirst);
									Cuemap.get(k).causeNode.add(findsecond);
								}else{
									Cuemap.get(k).causeNode.add(findsecond);
									Cuemap.get(k).causeNode.add(findfirst);
								}
							}else{
								Cuemap.get(k).causeNode.add(causepath.get(i));
								break;
							}
							
							break;
						} else if (!a) {
							Cuemap.get(k).causeNode.add(causepath.get(i));
							break;
						}

					}

				}
				while (iter2.hasNext()) {

					effect = iter2.next();
					List<Tree> corepath = root
							.dominationPath(Cuemap.get(k).coreCue.get(0));
					List<Tree> effectpath = root.dominationPath(effect);
					int[] sizerank = { corepath.size(), effectpath.size() };
					int sign2 = sizerank[0] < sizerank[1] ? sizerank[0]
							: sizerank[1];
					for (int i = 0; i < sign2; i++) {
						boolean b = corepath.get(i).equals(effectpath.get(i));

						if (!b && CauseCheckSet.contains(effectpath.get(i).value())) {
							Tree findfirst = effectpath.get(i);
							int count =0;
							for(Tree target : findfirst.parent().children()){
								if(target.nodeNumber(root)<=corepath.get(i).nodeNumber(root)){
									Cuemap.get(k).effectNode.add(target);
									count++;
								}
								
							}
							if(count==0){
								Cuemap.get(k).effectNode.add(effectpath.get(i));
							}

							
						} else if (!b) {
							Cuemap.get(k).effectNode.add(effectpath.get(i));
							break;
						}
					}

				}

			} else {
				// ---------------------------------------------------------------------------------------------------------
				// something is lost ! fix it
				if (Cuemap.get(k).cause.size() == 0) {
					while (iter2.hasNext()) {

						effect = iter2.next();
						
						List<Tree> corepath = root
								.dominationPath(Cuemap.get(k).coreCue.get(0));
						List<Tree> effectpath = root.dominationPath(effect);
						int[] sizerank = { corepath.size(), effectpath.size() };
						int sign2 = sizerank[0] < sizerank[1] ? sizerank[0]
								: sizerank[1];
						
						for (int i = 0; i < sign2; i++) {
							boolean b = corepath.get(i).equals(
									effectpath.get(i));

							if (!b) {
								Cuemap.get(k).effectNode.add(effectpath.get(i));
								break;
							}
						}

					}
					boolean out =false;
					for (TreeGraphNode c : Cuemap.get(k).coreCue) {
						if(out){
							break;
						}
						if (c.parent().value().equalsIgnoreCase("TO")
								|| c.parent().value().equalsIgnoreCase("IN")
								|| c.parent().value().equalsIgnoreCase("CC")) {
							Tree findvp = c;
							Tree findnp = c;
							boolean find = false;
							while (!findvp.value().equalsIgnoreCase("root")) {
								if (findvp.value().equalsIgnoreCase("VP")) {
									for (Tree d : findvp.parent().children()) {
										if (d.value().equalsIgnoreCase("NP")) {
											findnp = d;
											find = true;
										}
									}
								}
								if (find) {
									break;
								}
								findvp = findvp.parent();

							}
							if(find){
								Cuemap.get(k).causeNode.add(findnp);
								Cuemap.get(k).causeNode.add(findvp);
							}else{
								findvp=c;
								while (!findvp.value().equalsIgnoreCase("root")) {
									if (findvp.value().equalsIgnoreCase("NP")) {
										Cuemap.get(k).causeNode.add(findvp);
										out = true;
										break;
										
									}
									
									findvp = findvp.parent();

								}
							}
							

						}
					}
				}
				if (Cuemap.get(k).effect.size() == 0) {
					remolist.add(k);
					
				}

			}

		}
		for (Integer c : remolist) {
			Cuemap.remove(c);

		}
		// ----------------------------------------------------------------------------------------------------------------
		// transfer the causeNode and  effect Node into the Cue data structure  causeString and effectString
		Iterator<Entry<Integer, Cue>> iter = Cuemap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, Cue> cue = iter.next();
			if (cue.getValue().coreCue.size() != 0
					&& cue.getValue().causeNode.size() != 0) {
				
				String sf = "";
				for (Tree d : cue.getValue().causeNode) {
					StringBuilder sb1 = new StringBuilder();

					for (Tree c : d.getLeaves()) {
						sb1.append(c.value());
						sb1.append(" ");

					}
					String s1 = sb1.toString();
					if (s1.contains(" " + cue.getValue().coreCue.get(0).value()
							+ " ")) {
						s1 = s1.substring(
								0,
								s1.indexOf(" "+ cue.getValue().coreCue.get(0).value()
										+ " "));
					}
					
					sf=sf+s1.trim()+" ";
					

				}
				sf=sf.trim();
				cue.getValue().causeString =  sf;

			}
			if (cue.getValue().coreCue.size() != 0
					&& cue.getValue().effectNode.size() != 0) {
				String sf = "";
				for (Tree d : cue.getValue().effectNode) {
					StringBuilder sb2 = new StringBuilder();
					sb2.append(" ");
					for (Tree c : d.getLeaves()) {
						sb2.append(c.value());
						sb2.append(" ");

					}
					String s2 = sb2.toString();
					if (s2.contains(" " + cue.getValue().coreCue.get(0).value()
							+ " ")) {
						s2 = s2.substring(
								s2.indexOf(" "+ cue.getValue().coreCue.get(0).value()
										+ " "),s2.length());
					}
					sf=sf+s2;
				}
				cue.getValue().effectString =sf;
			}
		}
	
		

		return Cuemap;
	}

	
	private boolean checksides(HashMap<Integer, Cue> Cuemap, int key) {
		boolean check = true;
	
		if (Cuemap.get(key).cause.size() == 0
				&& Cuemap.get(key).effect.size() >= 2) {
			int diffmax = 100;
			TreeGraphNode change = new TreeGraphNode();
			for (TreeGraphNode d : Cuemap.get(key).effect) {
				if (diffmax > (d.index() - Cuemap.get(key).coreCue.get(0)
						.index())) {
					diffmax = d.index() - Cuemap.get(key).coreCue.get(0)
							.index();
					change = d;
				}
			}
			if(diffmax!=0){
				Cuemap.get(key).cause.add(change);
				Cuemap.get(key).effect.remove(change);
			}
			
		}
		if (Cuemap.get(key).effect.size() == 0
				&& Cuemap.get(key).cause.size() >= 2) {
			int diffmax = 100;
			TreeGraphNode change = new TreeGraphNode();
			for (TreeGraphNode d : Cuemap.get(key).cause) {
				if ((Cuemap.get(key).coreCue.get(0).index() - d.index()) < diffmax) {
					change = d;
					diffmax = Cuemap.get(key).coreCue.get(0).index() - d.index();
				}
			}
			if(diffmax!=0){
				Cuemap.get(key).effect.add(change);
				Cuemap.get(key).cause.remove(change);
			}
			

		}
		if ((Cuemap.get(key).cause.size() + Cuemap.get(key).effect.size()) >= 2) {
			check = false;
		}
		return check;
	}

	private int cuefilter(Set<TreeGraphNode> verbset, TypedDependency relation) {

		if (relation.reln().getShortName().equalsIgnoreCase("prep")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("infmod")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("conj")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("pobj")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("pcomp")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("xcomp")) {
			verbset.add(relation.dep());
			verbset.add(relation.gov());

			return 8;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("agent")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("acomp")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("ccomp")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("csubj")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("csubjpass")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("dobj")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("iobj")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("nsubj")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("xsubj")) {
			verbset.add(relation.gov());
			return 5;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("nsubjpass")) {
			verbset.add(relation.gov());
			return 6;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("partmod")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("rcmod")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("rel")) {
			verbset.add(relation.dep());
			return 7;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("mark")) {
			verbset.add(relation.dep());
			return 10;
		}
		if (relation.reln().getShortName().equalsIgnoreCase("advmod")
				&& relation.dep().value().equalsIgnoreCase("when")) {
			verbset.add(relation.dep());
			return 10;
		}

		if (relation.reln().getShortName().equalsIgnoreCase("advcl")) {
			verbset.add(relation.gov());
			return 9;
		}

		return 0;
	}

	private HashMap<Integer, Cue> merge(HashMap<Integer, Cue> Cuemap,
			int reserve, int del) {
		Iterator<TreeGraphNode> iter1 = Cuemap.get(del).cause.iterator();
		Iterator<TreeGraphNode> iter2 = Cuemap.get(del).effect.iterator();
		Iterator<TreeGraphNode> iter3 = Cuemap.get(del).coreCue.iterator();
		Iterator<String> iter4 = Cuemap.get(del).coreString.iterator();
		for (int j3 = 0; j3 < Cuemap.get(del).cause.size(); j3++) {
			Cuemap.get(reserve).cause.add(iter1.next());
		}

		for (int j3 = 0; j3 < Cuemap.get(del).effect.size(); j3++) {
			Cuemap.get(reserve).effect.add(iter2.next());
		}
		for (int j3 = 0; j3 < Cuemap.get(del).coreCue.size(); j3++) {
			Cuemap.get(reserve).coreCue.add(iter3.next());
		}
		for (int j3 = 0; j3 < Cuemap.get(del).coreString.size(); j3++) {
			Cuemap.get(reserve).coreString.add(iter4.next());
		}
		Set<TreeGraphNode> removecause = new HashSet<TreeGraphNode>();
		Set<TreeGraphNode> removeeffect = new HashSet<TreeGraphNode>();
		for (TreeGraphNode c : Cuemap.get(reserve).coreCue) {
			for (TreeGraphNode d : Cuemap.get(reserve).cause) {
				if (c.index() == d.index()) {
					removecause.add(d);
				}
			}
			for (TreeGraphNode d : Cuemap.get(reserve).effect) {
				if (c.index() == d.index()) {
					removeeffect.add(d);
				}
			}

		}
		Iterator<TreeGraphNode> iter5 = removecause.iterator();
		Iterator<TreeGraphNode> iter6 = removeeffect.iterator();
		while (iter5.hasNext()) {
			Cuemap.get(reserve).cause.remove(iter5.next());
		}
		while (iter6.hasNext()) {
			Cuemap.get(reserve).effect.remove(iter6.next());
		}
		// 消除与cue相同的 cause和effect

		return Cuemap;
	}
	
	

	@SuppressWarnings("serial")
	private boolean judgeforCS(String test, String test1, int i) {
		Set<String> special = new HashSet<String>() {
			{
				add("result");
				add("results");
				add("cause");
				add("causes");
				add("affect");
				add("affects");
				add("source");
				add("sources");
				add("reason");
				add("reasons");
				add("consequence");
				add("consequences");
				add("key");
				add("outcome");
				add("effect");
				add("response");
				add("grounds");
				add("drive");
				add("conclusion");
				add("movement");
				add("campaign");
				add("crusade");
				add("motive");
				add("rational");
				add("catalyst");
				add("influence");
				add("killer");
				add("engine");
				add("danger");
				add("judgement");
				add("intuition");
				add("occation");
				add("indication");
				add("contraindication");
			}
		};
		Set<String> advset = new HashSet<String>() {
			{
				add("RB");
				add("RBR");
				add("RBS");
			}
		};
		Set<String> adjset = new HashSet<String>() {
			{
				add("JJ");
				add("JJR");
				add("JJS");
			}
		};
		Set<String> BEset = new HashSet<String>() {
			{
				add("is");
				add("are");
				add("was");
				add("were");
			}
		};

		Set<String> VerbType = new HashSet<String>() {

			{
				add("VB");
				add("VBD");
				add("VBZ");
				add("VBG");
				add("VBN");
				add("VBP");
			}
		};
		Set<String> HaveBeen = new HashSet<String>() {
			{
				add("has");
				add("have");
				add("had");
				add("be");
				add("been");
			}
		};
		Set<String> ConjType = new HashSet<String>() {
			{
				add("TO");
				add("IN");
			}
		};
		boolean judge = false;
		switch (i) {
		case 0:
			judge = special.contains(test1) || test.equalsIgnoreCase("MD")
					|| test.equalsIgnoreCase("RP") || BEset.contains(test1)
					|| HaveBeen.contains(test1) || ConjType.contains(test)
					|| adjset.contains(test) || advset.contains(test);
			break;
		case 1:
			judge = special.contains(test1) || test.equalsIgnoreCase("MD")
					|| test.equalsIgnoreCase("RP") || BEset.contains(test1)
					|| HaveBeen.contains(test1) || ConjType.contains(test)
					|| adjset.contains(test) || advset.contains(test)
					|| VerbType.contains(test);
			break;
		case 8:
			judge = special.contains(test) ;
			break;

		}

		return judge;

	}

}


