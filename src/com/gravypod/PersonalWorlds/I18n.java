package com.gravypod.PersonalWorlds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.bukkit.ChatColor;

public class I18n {
	
	/** Get the default local */
	private final Locale defaulLocal = Locale.getDefault();
	
	/** Our native language */
	private final String language = defaulLocal.getLanguage();
	
	/** Out country */
	private final String country = defaulLocal.getCountry();
	
	/** Registered permission nodes */
	private final char colorCode = '&';
	
	/** Our native local */
	private final Locale locale = new Locale(language, country);
	
	/** default messages file */
	private final ResourceBundle messages = ResourceBundle.getBundle("Messages", locale);
	
	/** backup file. Messages_eu.prop */
	private final ResourceBundle defaultBundle = ResourceBundle.getBundle("Messages", Locale.ENGLISH);
	
	/** Messages in our plugin.DataFolder() */
	private final ResourceBundle customMessages;
	
	/** Our message map, all things from the message.prop that have been used */
	private final TreeMap<String, String> messageCache = new TreeMap<String, String>();
	
    public I18n(PersonalWorlds plugin) {
    	customMessages = ResourceBundle.getBundle("Messages", locale, new FileResClassLoader(I18n.class.getClassLoader(), plugin));
    }
	
	/**
	 * Get a message from out I18n data file.
	 * 
	 * @param message
	 *            - message title. Such as 'noPlayer'
	 * @return - What the set message really is
	 * 
	 */
	public String getColoredMessage(final String message) {
	
		if (messageCache.containsKey(message)) {
			return messageCache.get(message);
		}
		
		try {
			
			try {
				
				final String translatedMessage = ChatColor.translateAlternateColorCodes(colorCode, customMessages.getString(message));
				
				messageCache.put(message, translatedMessage);
				
				return translatedMessage;
				
			} catch (final MissingResourceException ex) {
				
				final String translatedMessage =  ChatColor.translateAlternateColorCodes(colorCode, messages.getString(message));
				
				messageCache.put(message, translatedMessage);
				
				return translatedMessage;
				
			}
			
		} catch (final MissingResourceException ex) {
			return defaultBundle.getString(message);
		}
		
	}
	
	/**
	 * Taken from essentials
	 */
	private static class FileResClassLoader extends ClassLoader {
		
		private final transient File dataFolder;
		
		public FileResClassLoader(final ClassLoader classLoader, final PersonalWorlds personalWorlds) {
		
			super(classLoader);
			dataFolder = personalWorlds.getDataFolder();
			
		}
		
		@Override
		public URL getResource(final String string) {
		
			final File file = new File(dataFolder, string);
			if (file.exists()) {
				try {
					return file.toURI().toURL();
				} catch (final MalformedURLException ex) {
				}
			}
			return super.getResource(string);
		}
		
		@Override
		public InputStream getResourceAsStream(final String string) {
		
			final File file = new File(dataFolder, string);
			
			if (file.exists()) {
				try {
					return new FileInputStream(file);
				} catch (final FileNotFoundException ex) {
				}
			}
			
			return super.getResourceAsStream(string);
			
		}
		
	}
	
}
