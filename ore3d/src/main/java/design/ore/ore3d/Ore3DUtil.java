package design.ore.ore3d;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import design.ore.base.util.Log;
import design.ore.base.util.ui.Navigation;
import design.ore.ore3d.data.core.Build;
import design.ore.ore3d.data.core.Transaction;
import design.ore.ore3d.data.pricing.BOMEntry;
import design.ore.ore3d.data.pricing.BOMPricing;
import design.ore.ore3d.data.pricing.RoutingEntry;
import design.ore.ore3d.data.pricing.RoutingPricing;
import design.ore.ore3d.ui.PopoutStage;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import lombok.Getter;

public class Ore3DUtil
{	
	@Getter private static Image brokenChainIcon;
	@Getter private static Image chainIcon;
	@Getter private static Image xIcon;
	static
	{
		try
		{
			brokenChainIcon = new Image("ui/icons/BrokenChainIcon.png");
			chainIcon = new Image("ui/icons/ChainIcon.png");
			xIcon = new Image("ui/icons/XIcon.png");
		}
		catch(Exception e) { Log.getLogger().warn(Log.formatThrowable("Failed to initialize UI elements!", e)); }
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
	{
	    List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	    list.sort(Entry.comparingByValue());
	
	    Map<K, V> result = new LinkedHashMap<>();
	    for (Entry<K, V> entry : list) { result.put(entry.getKey(), entry.getValue()); }
	
	    return result;
	}
	
	public static double squareInchesToSquareFeet(double squareInches) { return squareInches / 144.0; }
	
	public static BOMEntry duplicateBOMWithPricing(Transaction transaction, Build parent, BOMEntry entry, boolean isCustom)
	{
		Optional<BOMPricing> pricing = transaction.getPricing().getBom().stream().filter(bp -> bp.getInternalID().equals(entry.getId())).findFirst();
		if(pricing.isPresent()) return entry.duplicate(pricing.get().getCostPerUnit(), 1, parent, isCustom);
		else return entry.duplicate(1, parent, isCustom);
	}
	
	public static BOMEntry duplicateBOMWithPricing(Transaction transaction, Build parent, BOMEntry entry, BOMEntry originalEntry)
	{
		Optional<BOMPricing> pricing = transaction.getPricing().getBom().stream().filter(bp -> bp.getInternalID().equals(entry.getId())).findFirst();
		BOMEntry newEntry = null;
		if(pricing.isPresent()) newEntry = entry.duplicate(pricing.get().getCostPerUnit(), originalEntry.getUnoverriddenQuantityProperty().get(),
			parent, originalEntry.getCustomEntryProperty().get(), originalEntry.getIgnoreParentQuantityProperty().get());
		else newEntry = entry.duplicate(originalEntry.getUnoverriddenQuantityProperty().get(), parent,
			originalEntry.getCustomEntryProperty().get(), originalEntry.getIgnoreParentQuantityProperty().get());
		
		if(originalEntry.getQuantityOverriddenProperty().get()) newEntry.getOverridenQuantityProperty().set(originalEntry.getOverridenQuantityProperty().get());
		if(originalEntry.getMarginOverriddenProperty().get()) newEntry.getOverridenMarginProperty().set(originalEntry.getOverridenMarginProperty().get());
		
		return newEntry;
	}
	
	public static RoutingEntry duplicateRoutingWithPricing(Transaction transaction, Build parent, RoutingEntry entry, boolean isCustom, Double overriddenQuantity)
	{
		Optional<RoutingPricing> pricing = transaction.getPricing().getRoutings().stream().filter(bp -> bp.getId().equals(entry.getId())).findFirst();
		if(pricing.isPresent()) return entry.duplicate(pricing.get().getCostPerMinute(), 1d, parent,
			entry.getQuantityOverriddenProperty().get() ? entry.getOverridenQuantityProperty().get() : null, isCustom);
		else return entry.duplicate(1d, parent, entry.getQuantityOverriddenProperty().get() ? entry.getOverridenQuantityProperty().get() : null, isCustom);
	}
	
	public static RoutingEntry duplicateRoutingWithPricing(Transaction transaction, Build parent, RoutingEntry entry, RoutingEntry originalEntry)
	{
		Optional<BOMPricing> pricing = transaction.getPricing().getBom().stream().filter(bp -> bp.getInternalID().equals(entry.getId())).findFirst();
		RoutingEntry newEntry = null;
		if(pricing.isPresent()) newEntry = entry.duplicate(pricing.get().getCostPerUnit(), originalEntry.getUnoverriddenQuantityProperty().get(),
			parent, originalEntry.getCustomEntryProperty().get());
		else newEntry = entry.duplicate(originalEntry.getUnoverriddenQuantityProperty().get(), parent, originalEntry.getCustomEntryProperty().get());
		
		if(originalEntry.getQuantityOverriddenProperty().get()) newEntry.getOverridenQuantityProperty().set(originalEntry.getOverridenQuantityProperty().get());
		
		return newEntry;
	}
	
	public static final String DARK_STYLESHEET = "stylesheets/dark.css";
	public static final String BOW_STYLESHEET = "stylesheets/bow.css";
	public static final String REYMALA_STYLESHEET = "stylesheets/reymala.css";
	
	@Getter private static final ObservableList<String> styleOptions = FXCollections.observableArrayList(DARK_STYLESHEET, BOW_STYLESHEET, REYMALA_STYLESHEET);
	@Getter private static final Map<String, String> styleDisplayNames = Map.of("Dark", DARK_STYLESHEET, "Black & White", BOW_STYLESHEET, "Reymala", REYMALA_STYLESHEET);
	
	public static BooleanProperty showPopup(Pane content, Navigation navigation, String title, boolean useStylesheet)
	{
		PopoutStage<Void> stage = new PopoutStage<>(navigation, content, title, useStylesheet);
		navigation.registerMenuLockedSubStage(stage);
		stage.show();
		
		return stage.getCloseOnTrue();
	}
	
	public static void showPopupAndWait(Pane content, Navigation navigation, String title, boolean useStylesheet)
	{
		PopoutStage<Void> stage = new PopoutStage<Void>(navigation, content, title, useStylesheet);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.showAndWait();
	}
	
	@Getter private static String persistentPluginDataDir;
	public static void setPersistentPluginDataDir(String dir)
	{
		if(persistentPluginDataDir == null) persistentPluginDataDir = dir;
		else Log.getLogger().warn("Cannot set Appdata Dir as it is already set!");
	}
}
