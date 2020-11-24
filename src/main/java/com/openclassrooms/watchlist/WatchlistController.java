package com.openclassrooms.watchlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class WatchlistController {

	private List<WatchlistItem> watchlistItems = new  ArrayList<WatchlistItem>();
	private static int index = 1;
	
	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchlistItemForm(@Valid WatchlistItem watchlistItem, BindingResult bindingResult) {
	
		if(bindingResult.hasErrors()) {
            return new ModelAndView("watchlistItemForm");
        }
		
		WatchlistItem existingItem = findWatchlistItem(watchlistItem.getId());
		
		if(existingItem==null) {
			watchlistItem.setId(index++);
			watchlistItems.add(watchlistItem);
		}else {
			existingItem.setTitle(watchlistItem.getTitle());
			existingItem.setRating(watchlistItem.getRating());
			existingItem.setPriority(watchlistItem.getPriority());
			existingItem.setComment(watchlistItem.getComment());
		}
		RedirectView redirect = new RedirectView();
		redirect.setUrl("/watchlist");
		return new ModelAndView(redirect);
	}
	
	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchlistItemForm(@RequestParam(required=false)Integer id) {
		
		String viewName= "watchlistItemForm";
		WatchlistItem watchlistItem = findWatchlistItem(id);
		if(watchlistItem == null) {
			watchlistItem = new WatchlistItem();
		}
		
		Map<String, Object> model = new HashMap<String,Object>();
		model.put("watchlistItem", watchlistItem);
		return new ModelAndView(viewName,model);
		
	}
	
	private WatchlistItem findWatchlistItem(Integer id) {
		for (WatchlistItem watchlistItem : watchlistItems) {
			if(watchlistItem.getId().equals(id))
				return watchlistItem;
		}
		return null;
	}

	@GetMapping("/watchlist")
	public ModelAndView getWatchlist() {
		String viewName= "watchlist";
		
//		watchlistItems.clear();
//		watchlistItems.add(new WatchlistItem(index++, "Lion King","8.5","high","Hakuna matata!"));
//		watchlistItems.add(new WatchlistItem(index++, "Frozen","7.5","medium","Let it go!"));
//		watchlistItems.add(new WatchlistItem(index++, "Cars","7.1","low","Go go go!"));
//		watchlistItems.add(new WatchlistItem(index++, "Wall-E","8.4","high","You are crying!"));
//		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("watchlistItems", watchlistItems);
		model.put("numberOfMovies", watchlistItems.size());
		
		return new ModelAndView(viewName,model);
	}
}
