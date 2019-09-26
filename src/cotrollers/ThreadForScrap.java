package cotrollers;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import application.AlertBox;
import javafx.application.Platform;

/**
 * this class extends the Thread class
 * 
 * <p>to run two thread, one is itself for insert date to database,
 * other is static (named search)and implements Runnable class 
 * for Scraping information from Internet and judge whether it can be inserted.
 *  </p>
 *
 * @author Huang Yu'an
 */
public class ThreadForScrap extends Thread {
	private LinkedList<WebScraping> wspS;
	private loadWebSource lw;
	AlertBox albox;
	/**
	 * <p>
	 * it is static and implements Runnable class for Scraping information from Internet.
	 * It will use isScrap method to judge.<br>
	 *   if can ,it will add it to a @Linkedlist;<br>
	 *   if not, this thread will break;
	 *  </p>
	 *  @see loadWebSource#isScrap
	 * @author Huang Yu'an
	 */
	private static Thread search;

	/**
	 * <b> a construcotr to creat a object.</b>
	 * @param cnf object which has information to initial
	 */
	public ThreadForScrap(loadCNF cnf) {
		super();
		wspS = new LinkedList<>();
		lw = new loadWebSource(null, cnf);
		Platform.runLater(()-> {
            	albox = new AlertBox();
        		albox.Alert("Loading", "Searching, do not closed the window.");
        });
		iniSearch();
		search.start();	
	}

	/**
	 * <b>initial search Thread object</b>
	 */
	private void iniSearch() {
		search = new Thread(() -> {
			int i = 1;
			Platform.runLater(()->albox.label.setText("Loading..."));
			while (i <= 2000) {
				lw.setWsp(new WebScraping(i));
				try {
					if (!lw.isScrap())
						break;
					wspS.add(lw.getWsp());
					String temp = "Loading..."+i;
					Platform.runLater(()->albox.label.setText(temp));
					i++;
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.err.println("The searching thread is interrupted!");
					break;
				} catch (NoSuchElementException e) {
					Platform.runLater(()->albox.label.setText("Please check the Internet."));
					try {
						Thread.sleep(3500);
					} catch (InterruptedException e1) {
					} catch (IllegalStateException e1) {
						System.err.println(e.getMessage());
					}
					return;
				} catch (IllegalStateException e) {
					System.err.println(e.getMessage());
				}
			}
			Platform.runLater(()->albox.label.setText("Loading finished."));
		});
	}
	
	/**
	 * <p>
	 * run method to set the thread fot this object, to load every data which in the
	 * linkedlist into the database file, and it will guruntee that data is insert
	 * from earliest to now
	 * </p>
	 */
	public void run() {
		try {
			search.join();
		} catch (InterruptedException e1) {
			System.err.println("the loading thread is interrupted!");
		}
		ListIterator<WebScraping> it = wspS.listIterator(wspS.size());
		while (it.hasPrevious()) {
			try {
				sleep(2000);
				lw.setWsp(it.previous());
				lw.insert();
			} catch (InterruptedException e) {
				System.err.println("the loading thread is interrupted!");
				break;
			} catch (IllegalStateException e) {
				System.err.println(e.getMessage());
			}
		}
		wspS = null;
		lw.close();
		Platform.runLater(()->albox.window.close());
	}
}
