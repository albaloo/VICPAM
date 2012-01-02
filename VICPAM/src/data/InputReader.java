/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package data;
/**
 * @author Roshanak Zilouchian
 */
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pres.MainWindow;

import domain.Application;
import domain.Player;
import domain.Replication;
import domain.Session;
import domain.Share;
import domain.Show;
import domain.ShowShare;
import domain.UserActivity.SharedDevice;

public class InputReader {

	private Session session;
	private DefaultHandler handler;
	
	public void parseInputFile(String filename, MainWindow mainWindow, Session session) {
		this.session = session;
		
		handler = new EntryHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(filename, handler);
		} catch (Exception e) {
			String errorMessage = "Error parsing " + filename + ": " + e;
			System.err.println(errorMessage);
			e.printStackTrace();
		}
		session.setEnd(new Time(((EntryHandler)handler).getStartTime() + ((EntryHandler)handler).getTotalDuration()));
		session.setStart(new Time(((EntryHandler)handler).getStartTime()));
		session.setSelectedDuration(((EntryHandler)handler).getTotalDuration()/5);
		fillMap(((EntryHandler)handler).getEntries());
		mainWindow.setSession(session);
	}
	
	private void fillMap(ArrayList<Entry> entries){
		
		HashMap<String, Player> players = new HashMap<String, Player>();
		HashMap<String, ShowShare> showShares = new HashMap<String, ShowShare>();
		
		ArrayList<Entry> allReplications = new ArrayList<Entry>();

		double maxDuration = 0;
		for (Entry entry : entries) {
			if(entry.getEndTime().getTime() - entry.getStartTime().getTime() > maxDuration)
				maxDuration = entry.getEndTime().getTime() - entry.getStartTime().getTime();
		}
		
		Session.setMaxDuration(new Double(maxDuration/((EntryHandler)handler).getTotalDuration()).intValue());
		System.out.println("Session.getMaxDuration(): " + maxDuration);
		
		for (Entry entry : entries) {
			Player currentPlayer;
			if(players.get(entry.getUser()) == null || players.get(entry.getUser()).equals("")){
				currentPlayer= new Player(new String(entry.getUser()));
				players.put(entry.getUser(), currentPlayer);
			}else{
				currentPlayer = players.get(entry.getUser());
			}
			SharedDevice sharedDevice;
			if(entry.getDevice().equals("personal"))
				sharedDevice = SharedDevice.PERSONAL;
			else
				sharedDevice = SharedDevice.SHAREDDISPLAY;
			if(entry.getActivity().equals("show")){
				Show currentShow = new Show(new Application(entry.getApplication()), currentPlayer, entry.getStartTime(), entry.getEndTime(), session.getDuration(), sharedDevice);
				currentPlayer.addShow(currentShow);
				if(entry.getDevice().equals("shared display"))
					currentPlayer.addToSharedDisplay(currentShow);
				showShares.put(entry.getEntryID(), currentShow);
			}else if(entry.getActivity().equals("share")){
				Share currentShare = new Share(new Application(entry.getApplication()), currentPlayer, entry.getStartTime(), entry.getEndTime(), session.getDuration(), sharedDevice);
				currentPlayer.addShare(currentShare);
				if(entry.getDevice().equals("shared display"))
					currentPlayer.addToSharedDisplay(currentShare);
				showShares.put(entry.getEntryID(), currentShare);
			}else if(entry.getActivity().equals("replication")){
				allReplications.add(entry);
			}

		}

		for (Entry entry : allReplications) {
			ShowShare fromEntry = showShares.get(entry.getFromEntryID());
			SharedDevice sharedDevice;
			if(entry.getDevice().equals("personal"))
				sharedDevice = SharedDevice.PERSONAL;
			else
				sharedDevice = SharedDevice.SHAREDDISPLAY;
			Replication currentReplication = new Replication(fromEntry, players.get(entry.getUser()), entry.getStartTime(), entry.getEndTime(), sharedDevice);
			fromEntry.addReplication(currentReplication);
		}
		
		for (Player player : players.values()) {
			player.init();
			session.addPlayer(player);
		}

	}


	public class EntryHandler extends DefaultHandler {

		/**
		 * \emph{$<$entry$>$}
		 * \emph{$<$entry ID$>$} 
		 * \emph{$<$totalduration$>$}  
		 * \emph{$<$user$>$}user name
		 * \emph{$<$application$>$}the application name 
		 * \emph{$<$activity$>$}
		 * \emph{$<$fromentryID$>$}
		 * \emph{$<$device$>$}personal device or shared dispay 
		 * \emph{$<$time$>$}which contains \emph{$<$start$>$} and \emph{$<$end$>$}
		 */
		private boolean collectEntryID = false;
		private boolean collectTotalDuration = false;
		private boolean collectUser = false;
		private boolean collectApplication = false;
		private boolean collectActivity = false;
		private boolean collectFromEntryID = false;
		private boolean collectDevice = false;
		private boolean collectStartTime = false;
		private boolean collectEndTime = false;
		
		private long totalDuration;
		private Entry currentEntry;
		private ArrayList<Entry> entries = new ArrayList<Entry>();
		
		public long getTotalDuration(){
			return totalDuration;
		}
		
		public long getStartTime(){
			long startTime = 0;
			if(entries.size() != 0)
				startTime = entries.get(0).getStartTime().getTime();
			for (Entry entry : entries) {
				if(entry.getStartTime().getTime()< startTime)
					startTime = entry.getStartTime().getTime();
			}
			
			return startTime;
		}
		public ArrayList<Entry> getEntries(){
			return entries;
		}
		public void startElement(String namespaceUri, String localName,
				String qualifiedName, Attributes attributes)
				throws SAXException {
			if(qualifiedName.equals("entryID")){
				collectEntryID = true;
				currentEntry = new Entry();
			} else if (qualifiedName.equals("totalduration")) {
				collectTotalDuration = true;
			} else if (qualifiedName.equals("user")) {
				collectUser = true;
			} else if (qualifiedName.equals("application")) {
				collectApplication = true;
			} else if (qualifiedName.equals("activity")) {
				collectActivity = true;
			}else if (qualifiedName.equals("fromentryID")) {
				collectFromEntryID = true;
			}else if (qualifiedName.equals("device")) {
				collectDevice = true;
			} else if (qualifiedName.equals("start")) {
				collectStartTime = true;
			} else if (qualifiedName.equals("end")) {
				collectEndTime = true;
			}
		}

		public void endElement(String namespaceUri, String localName,
				String qualifiedName) throws SAXException {
			if(qualifiedName.equals("entryID")){
				collectEntryID = false;
			} else if (qualifiedName.equals("totalduration")) {
				collectTotalDuration = false;
			} else if (qualifiedName.equals("user")) {
				collectUser = false;
			} else if (qualifiedName.equals("application")) {
				collectApplication = false;
			} else if (qualifiedName.equals("activity")) {
				collectActivity = false;
			} else if (qualifiedName.equals("fromentryID")) {
				collectFromEntryID = false;
			}else if (qualifiedName.equals("device")) {
				collectDevice = false;
			} else if (qualifiedName.equals("start")) {
				collectStartTime = false;
			} else if (qualifiedName.equals("end")) {
				collectEndTime = false;
				entries.add(currentEntry);
			}
		}

		public void characters(char[] chars, int startIndex, int endIndex) {
			if (collectEntryID || collectTotalDuration || collectUser || collectApplication || collectActivity || collectFromEntryID || collectDevice || collectStartTime || collectEndTime) {
				String dataString = new String(chars, startIndex, endIndex)
						.trim();
				if(dataString != null && !dataString.equals("/n") && !dataString.equals("") && !dataString.equals(" ")){

					if(collectEntryID){
						currentEntry.setEntryID(dataString);
					}else if (collectTotalDuration) {
						try {
							totalDuration = Long.parseLong(dataString);//(Time)DateFormat.getTimeInstance(DateFormat.LONG).parse((dataString));//Integer.parseInt(dataString);
						} catch (NumberFormatException nfe) {
							System.err.println("Ignoring malformed count: "
									+ dataString);
						}
					} else if (collectUser) {
						currentEntry.setUser(new String(dataString));
					}else if (collectApplication) {
						currentEntry.setApplication(dataString);
					}else if (collectActivity) {
						currentEntry.setActivity(dataString);
					}else if (collectFromEntryID) {
						currentEntry.setFromEntryID(dataString);
					}else if (collectDevice) {
						currentEntry.setDevice(dataString);
					}else if (collectStartTime) {
						try {
							currentEntry.setStartTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM).parse((dataString)).getTime()));
						} catch (NumberFormatException nfe) {
							System.err.println("Ignoring malformed count: "
									+ dataString);
						} catch (ParseException e) {
							System.err.println("Start Time cannot be parsed");
							e.printStackTrace();
						}
					}else if (collectEndTime) {
						try {
							currentEntry.setEndTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM).parse((dataString)).getTime()));
						} catch (NumberFormatException nfe) {
							System.err.println("Ignoring malformed count: "
									+ dataString);
						} catch (ParseException e) {
							System.err.println("End Time cannot be parsed");
							e.printStackTrace();
						}
					}
				}
			}
		}

		public void endDocument() throws SAXException {
		}

	}

}
