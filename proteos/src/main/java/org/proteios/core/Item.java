/*
 $Id: Item.java 3677 2010-04-20 12:02:33Z gregory $

 Copyright (C) 2006, 2007 Fredrik Levander, Gregory Vincic, Olle Mansson

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Proteios is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 02111-1307, USA.
 */
package org.proteios.core;

import org.proteios.core.data.AcquisitionData;
import org.proteios.core.data.AnnotationData;
import org.proteios.core.data.AnnotationSetData;
import org.proteios.core.data.AnnotationTypeData;
import org.proteios.core.data.BasicData;
import org.proteios.core.data.BioSourceData;
import org.proteios.core.data.HardwareConfigurationData;
import org.proteios.core.data.HardwareData;
import org.proteios.core.data.HardwareTypeData;
import org.proteios.core.data.ItemKeyData;
import org.proteios.core.data.MeasuredAreaData;
import org.proteios.core.data.ObservedModificationData;
import org.proteios.core.data.PeakData;
import org.proteios.core.data.PeakListData;
import org.proteios.core.data.PeakListSetData;
import org.proteios.core.data.PeptideData;
import org.proteios.core.data.PrecursorData;
import org.proteios.core.data.ProjectKeyData;
import org.proteios.core.data.SampleData;
import org.proteios.core.data.SearchResultData;
import org.proteios.core.data.UserData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class defines constants for various items in Proteios.
 * 
 * @author Samuel
 * @version 2.0
 * @base.modified $Date: 2010-04-20 05:02:33 -0700 (Tue, 20 Apr 2010) $
 */
public enum Item
{

//	/**
//	 * Not an item as such. Used for setting system permissions to role keys.
//	 *
//	 * @see Install
//	 * @see SessionControl#hasSystemPermission(Permission)
//	 */
 	SYSTEM(1, "System", null, null, null, null),
//	/**
//	 * The item is a {@link User}.
//	 */
 	USER(21, "User", "usr", User.class, UserData.class,
 			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Group}.
//	 */
//	GROUP(22, "Group", "grp", Group.class, GroupData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Role}.
//	 */
//	ROLE(23, "Role", "rle", Role.class, RoleData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Project}.
//	 */
//	PROJECT(24, "Project", "prj", Project.class, ProjectData.class,
//			DefinedPermissions.ownable),
//	/**
//	 * The item is an {@link ItemKey}.
//	 */
 	ITEMKEY(25, "Item key", "ik", ItemKey.class, ItemKeyData.class, null),
//	/**
//	 * The item is a {@link ProjectKey}.
//	 */
 	PROJECTKEY(26, "Project key", "pk", ProjectKey.class, ProjectKeyData.class,
 			null),
//	/**
//	 * The item is a {@link RoleKey}.
//	 */
//	ROLEKEY(27, "Role key", "rk", RoleKey.class, RoleKeyData.class, null),
//	/**
//	 * The item is an {@link Client}.
//	 */
//	CLIENT(41, "Client", "cli", Client.class, ClientData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link Session}.
//	 */
//	SESSION(43, "Session", "ses", Session.class, SessionData.class,
//			DefinedPermissions.read),
//	/**
//	 * The item is an {@link Setting}.
//	 */
//	SETTING(44, "Setting", "set", Setting.class, SettingData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is an {@link GlobalDefaultSetting}.
//	 */
//	GLOBALDEFAULTSETTING(45, "Global default setting", "gds",
//			GlobalDefaultSetting.class, GlobalDefaultSettingData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Quota}.
//	 */
//	QUOTA(61, "Quota", "qta", Quota.class, QuotaData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link QuotaType}.
//	 */
//	QUOTATYPE(62, "Quota type", "qtp", QuotaType.class, QuotaTypeData.class,
//			DefinedPermissions.write),
//	/**
//	 * The item is a {@link DiskUsage}.
//	 */
//	DISKUSAGE(63, "Disk usage", "du", DiskUsage.class, DiskUsageData.class,
//			DefinedPermissions.read),
//	/**
//	 * The item is a {@link File}.
//	 */
//	FILE(81, "File", "fle", File.class, FileData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link FileType}.
//	 */
//	FILETYPE(82, "File type", "ftp", FileType.class, FileTypeData.class,
//			DefinedPermissions.write),
//	/**
//	 * The item is a {@link Directory}.
//	 */
//	DIRECTORY(83, "Directory", "dir", Directory.class, DirectoryData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link MimeType}.
//	 */
//	MIMETYPE(84, "Mime type", "mtp", MimeType.class, MimeTypeData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Protocol}.
//	 */
//	PROTOCOL(101, "Protocol", "prl", Protocol.class, ProtocolData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link ProtocolType}.
//	 */
//	PROTOCOLTYPE(102, "Protocol type", "ptp", ProtocolType.class,
//			ProtocolTypeData.class, DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Hardware}.
//	 */
 HARDWARE(121, "Hardware", "hw", Hardware.class, HardwareData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link HardwareType}.
//	 */
 	HARDWARETYPE(122, "Hardware type", "htp", HardwareType.class,
 			HardwareTypeData.class, DefinedPermissions.write),
//	/**
//	 * The item is a {@link Software}.
//	 */
//	SOFTWARE(123, "Software", "sw", Software.class, SoftwareData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SoftwareType}.
//	 */
//	SOFTWARETYPE(124, "Software type", "stp", SoftwareType.class,
//			SoftwareTypeData.class, DefinedPermissions.write),
//	/**
//	 * The item is a {@link News}.
//	 */
//	NEWS(141, "News", "nws", News.class, NewsData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link Message}.
//	 */
//	MESSAGE(142, "Message", "msg", Message.class, MessageData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is an {@link AnnotationSet}.
//	 */
 	ANNOTATIONSET(181, "Annotation set", "ans", AnnotationSet.class,
 			AnnotationSetData.class, DefinedPermissions.basic),
//	/**
//	 * The item is an {@link Annotation}.
//	 */
 	ANNOTATION(182, "Annotation", "ann", Annotation.class,
 			AnnotationData.class, DefinedPermissions.basic),
//	/**
//	 * The item is an {@link AnnotationType}.
//	 */
 	ANNOTATIONTYPE(183, "Annotation type", "atp", AnnotationType.class,
 			AnnotationTypeData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link BioSource}.
//	 */
 	BIOSOURCE(201, "Biosource", "bs", BioSource.class, BioSourceData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Sample}.
//	 */
 	SAMPLE(202, "Sample", "smp", Sample.class, SampleData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link Extract}.
//	 */
//	EXTRACT(203, "Extract", "xtr", Extract.class, ExtractData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link LabeledExtract}.
//	 */
//	LABELEDEXTRACT(204, "Labeled extract", "lbe", LabeledExtract.class,
//			LabeledExtractData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link BioMaterialEvent}.
//	 */
//	BIOMATERIALEVENT(205, "Biomaterial event", "bme", BioMaterialEvent.class,
//			BioMaterialEventData.class, null),
//	/**
//	 * The item is a {@link Label}.
//	 */
//	LABEL(206, "Label", "lbl", Label.class, LabelData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link PluginType}
//	 */
//	PLUGINTYPE(281, "Plugin type", "plt", PluginType.class,
//			PluginTypeData.class, DefinedPermissions.basic),
//	/**
//	 * The item is a {@link PluginDefinition}
//	 */
//	PLUGINDEFINITION(282, "Plugin definition", "pld", PluginDefinition.class,
//			PluginDefinitionData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link PluginConfiguration}
//	 */
//	PLUGINCONFIGURATION(283, "Plugin configuration", "plc",
//			PluginConfiguration.class, PluginConfigurationData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Job}
//	 */
//	JOB(284, "Job", "job", Job.class, JobData.class, DefinedPermissions.ownable),
//	/**
//	 * The item is a {@link HardwareConfiguration}
//	 */
 	HARDWARECONFIGURATION(401, "HardwareConfiguration", "hwc",
 			HardwareConfiguration.class, HardwareConfigurationData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SoftwareConfiguration}
//	 */
//	SOFTWARECONFIGURATION(402, "SoftwareConfiguration", "swc",
//			SoftwareConfiguration.class, SoftwareConfigurationData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * Not an item as such. Used as a convenient way to set an offset for the
//	 * index value of Proteios constants.
//	 */
 	PROTEIOS_OFFSET(500, "Proteios_offset", null, null, null, null),
//	/**
//	 * The item is a {@link PeakListSet}
//	 */
 	PROTEIOS_PEAKLISTSET(PROTEIOS_OFFSET.value + 1, "PeakListSet", "pls",
 			PeakListSet.class, PeakListSetData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link PeakList}
//	 */
 	PROTEIOS_PEAKLIST(PROTEIOS_OFFSET.value + 2, "PeakList", "pl",
 		PeakList.class, PeakListData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link DataProcessingStep}
//	 */
//	PROTEIOS_DATAPROCESSINGSTEP(PROTEIOS_OFFSET.value + 3,
//			"DataProcessingStep", "dps", DataProcessingStep.class,
//			DataProcessingStepData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Precursor}
//	 */
 	PROTEIOS_PRECURSOR(PROTEIOS_OFFSET.value + 4, "Precursor", "pc",
 			Precursor.class, PrecursorData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Peak}
//	 */
 	PROTEIOS_PEAK(PROTEIOS_OFFSET.value + 5, "Peak", "pk", Peak.class,
 			PeakData.class, DefinedPermissions.basic),
//	/**
//	 * The item is an {@link Acquisition}
//	 */
 	PROTEIOS_ACQUISITION(PROTEIOS_OFFSET.value + 6, "Acquisition", "ac",
 			Acquisition.class, AcquisitionData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link SpectrumSearch}
//	 */
//	PROTEIOS_SPECTRUMSEARCH(PROTEIOS_OFFSET.value + 7, "SpectrumSearch", "sps",
//			SpectrumSearch.class, SpectrumSearchData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link DigestParameter}
//	 */
//	PROTEIOS_DIGESTPARAMETER(PROTEIOS_OFFSET.value + 8, "DigestParameter",
//			"dip", DigestParameter.class, DigestParameterData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link SearchDatabase}
//	 */
//	PROTEIOS_SEARCHDATABASE(PROTEIOS_OFFSET.value + 9, "SearchDatabase", "sdb",
//			SearchDatabase.class, SearchDatabaseData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SearchResult}
//	 */
 	PROTEIOS_SEARCHRESULT(PROTEIOS_OFFSET.value + 10, "SearchResult", "ser",
 			SearchResult.class, SearchResultData.class,
 		DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Protein}
//	 */
// 	PROTEIOS_PROTEIN(PROTEIOS_OFFSET.value + 11, "Protein", "pro",
// 			TandemFactory.TandemFactory.Protein.class, ProteinData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Peptide}
//	 */
 	PROTEIOS_PEPTIDE(PROTEIOS_OFFSET.value + 12, "Peptide", "pep",
 			Peptide.class, PeptideData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link Hit}
//	 */
//	PROTEIOS_HIT(PROTEIOS_OFFSET.value + 14, "Hit", "hit", Hit.class,
//			HitData.class, DefinedPermissions.basic),
//	/**
//	 * The item is a {@link MeasuredArea}
//	 */
 	PROTEIOS_MEASUREDAREA(PROTEIOS_OFFSET.value + 15, "MeasuredArea", "ma",
 		MeasuredArea.class, MeasuredAreaData.class,
 			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link GelElectrophoresis}
//	 */
//	PROTEIOS_GELELECTROPHORESIS(PROTEIOS_OFFSET.value + 16,
//			"GelElectrophoresis", "geps", GelElectrophoresis.class,
//			GelElectrophoresisData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link IPG}
//	 */
//	PROTEIOS_IPG(PROTEIOS_OFFSET.value + 17, "IPG", "ipg", IPG.class,
//			IPGData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SeparationMethod}
//	 */
//	PROTEIOS_SEPARATIONMETHOD(PROTEIOS_OFFSET.value + 18, "SeparationMethod",
//			"sm", SeparationMethod.class, SeparationMethodData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link GelImageAnalysisEvent}
//	 */
//	PROTEIOS_GELIMAGEANALYSISEVENT(PROTEIOS_OFFSET.value + 19,
//			"GelImageAnalysisEvent", "giae", GelImageAnalysisEvent.class,
//			GelImageAnalysisEventData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link GelScanEvent}
//	 */
//	PROTEIOS_GELSCANEVENT(PROTEIOS_OFFSET.value + 20, "GelScanEvent", "gse",
//			GelScanEvent.class, GelScanEventData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SeparationEvent}
//	 */
//	PROTEIOS_SEPARATIONEVENT(PROTEIOS_OFFSET.value + 21, "SeparationEvent",
//			"se", SeparationEvent.class, SeparationEventData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link StainingEvent}
//	 */
//	PROTEIOS_STAININGEVENT(PROTEIOS_OFFSET.value + 22, "StainingEvent", "se",
//			StainingEvent.class, StainingEventData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link ObservedModification}
//	 */
 	PROTEIOS_OBSERVEDMODIFICATION(PROTEIOS_OFFSET.value + 23,
 			"ObservedModification", "om", ObservedModification.class,
 			ObservedModificationData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link SearchModification}
//	 */
//	PROTEIOS_SEARCHMODIFICATION(PROTEIOS_OFFSET.value + 24,
//			"SearchModification", "sm", SearchModification.class,
//			SearchModificationData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link CreationEvent}
//	 */
//	PROTEIOS_CREATIONEVENT(PROTEIOS_OFFSET.value + 25, "CreationEvent", "cev",
//			CreationEvent.class, CreationEventData.class,
//			DefinedPermissions.basic),
//	/**
//	 * The item is a {@link BioMaterial}. Was introduced when trying to display
//	 * any biomaterial by clicking on it.
//	 */
//	PROTEIOS_BIOMATERIAL(PROTEIOS_OFFSET.value + 26, "BioMaterial", "bml",
//			BioMaterial.class, BioMaterialData.class, DefinedPermissions.basic),
//	/**
//	 * The item is an {@link UpdateEvent}
//	 */
//	PROTEIOS_UPDATEEVENT(PROTEIOS_OFFSET.value + 27, "UpdateEvent", "ue",
//			UpdateEvent.class, UpdateEventData.class,
//			DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link LiquidChromatography}
//	 */
//	PROTEIOS_LIQUIDCHROMATOGRAPHY(PROTEIOS_OFFSET.value + 28,
//			"LiquidChromatography", "lc", LiquidChromatography.class,
//			LiquidChromatographyData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link XTandemParameterSetStorage}
//	 */
//	PROTEIOS_XTANDEMPARAMETERSETSTORAGE(PROTEIOS_OFFSET.value + 29,
//			"XTandemParameterSetStorage", "xtpss", XTandemParameterSetStorage.class,
//			XTandemParameterSetStorageData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is an {@link OMSSAParameterSetStorage}
//	 */
////	PROTEIOS_OMSSAPARAMETERSETSTORAGE(PROTEIOS_OFFSET.value + 30,
////			"OMSSAParameterSetStorage", "omssapss", OMSSAParameterSetStorage.class,
////			OMSSAParameterSetStorageData.class, DefinedPermissions.shareable),
////	/**
////	 * The item is a {@link MascotParameterSetStorage}
////	 */
////	PROTEIOS_MASCOTPARAMETERSETSTORAGE(PROTEIOS_OFFSET.value + 31,
////			"MascotParameterSetStorage", "mascotpss", MascotParameterSetStorage.class,
////			MascotParameterSetStorageData.class, DefinedPermissions.shareable),
//	/**
//	 * The item is a {@link MascotParameterSetStorage}
//	 */
//	PROTEIOS_FEATURE(PROTEIOS_OFFSET.value + 32,
//			"Feature", "feat", Feature.class,
//			FeatureData.class, DefinedPermissions.basic);
    ;
	/**
	 * The largest id value used to verify that there are no two items with the
	 * same id
	 */
	static int MAX_VALUE = 0;
	/**
	 * Maps the id value with the enumerated value, used to verify that only one
	 * id value is mapped to one enumerated value
	 */
	private static final Map<Integer, Item> valueMapping = new HashMap<Integer, Item>();
	/**
	 * Maps the enumeration with an alias, used to verify that there is only one
	 * alias for each item
	 */
	private static final Map<String, Item> aliasMapping = new HashMap<String, Item>();
	/**
	 * Maps a wrapper class with the enumerated value
	 */
	@SuppressWarnings("unchecked")
	private static final Map<Class<? extends BasicItem>, Item> itemClassMapping = new HashMap<Class<? extends BasicItem>, Item>();
	/**
	 * Maps a data class with the enumerated value
	 */
	private static final Map<Class<? extends BasicData>, Item> dataClassMapping = new HashMap<Class<? extends BasicData>, Item>();
	/*
	 * Verify that all enumerated values have one wrapper class, one data class,
	 * a unique alias and a unique id
	 */
	static
	{
		for (Item item : Item.values())
		{
			int value = item.getValue();
			if (MAX_VALUE < value)
				MAX_VALUE = value;
			Item i = valueMapping.put(value, item);
			assert i == null : "Another item with the value " + value + " already exists: " + i;
			i = aliasMapping.put(item.getAlias(), item);
			assert i == null : "Another item with the alias " + item.getAlias() + " already exists: " + i;
			if (item.getItemClass() != null)
			{
				i = itemClassMapping.put(item.getItemClass(), item);
				assert i == null : "Another item for class " + item
					.getItemClass() + " already exists: " + i;
			}
			if (item.getDataClass() != null)
			{
				i = dataClassMapping.put(item.getDataClass(), item);
				assert i == null : "Another item for class " + item
					.getDataClass() + " already exists: " + i;
			}
		}
	}
	private final int value;
	private final String displayValue;
	private final String alias;
	@SuppressWarnings("unchecked")
	private final Class<? extends BasicItem> itemClass;
	private final Class<? extends BasicData> dataClass;
	private final Set<Permission> definedPermissions;
	private final Method getById;
	@SuppressWarnings("unchecked")
	private final Constructor<? extends BasicItem> constructor;


	@SuppressWarnings("unchecked")
	private Item(int value, String displayValue, String alias,
			Class<? extends BasicItem> itemClass,
			Class<? extends BasicData> dataClass,
			Set<Permission> definedPermissions)
	{
		this.value = value;
		this.displayValue = displayValue;
		this.alias = alias;
		this.itemClass = itemClass;
		this.dataClass = dataClass;
		this.definedPermissions = definedPermissions;
		Method tempGetById = null;
		Constructor<? extends BasicItem> tempConstructor = null;
		if (itemClass != null)
		{
//			try
//			{
//				tempGetById = itemClass.getDeclaredMethod("getById",
//					DbControl.class, int.class);
//			}
//			catch (NoSuchMethodException ex)
//			{}
			try
			{
				tempConstructor = itemClass.getDeclaredConstructor(dataClass);
			}
			catch (NoSuchMethodException ex)
			{}
		}
		getById = tempGetById;
		constructor = tempConstructor;
	}


	@Override
	public String toString()
	{
		return displayValue;
	}


	/**
	 * Get the integer value that is used when storing an item to the database.
	 */
	public int getValue()
	{
		return value;
	}


	/**
	 * Get the alias for this item, which is used in queries,
	 * 
	 * @see org.proteios.core.query.Query#getRootAlias()
	 */
	public String getAlias()
	{
		return alias;
	}


	/**
	 * Get the class object that is used to handle items of this type.
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends BasicItem> getItemClass()
	{
		return itemClass;
	}


	/**
	 * Get the data object that is used to store information in the database for
	 * items of this type.
	 */
	public Class<? extends BasicData> getDataClass()
	{
		return dataClass;
	}


	/**
	 * Get the permissions which are meaningful to assign to an item if this
	 * type. This is not used internally by the core, but is can be used by
	 * client applications to dynamically display a proper input form while
	 * assigning permissions.
	 * 
	 * @return A <code>Set</code> containing the permissions
	 */
	public Set<Permission> getDefinedPermissions()
	{
		return definedPermissions;
	}


	/**
	 * Get the item of the type represented by this enumeration constant with
	 * the specified id. Ie. <code>Item.SAMPLE.getById()</code> is the same as
	 * <code>Sample.getById</code>.
	 * 
	 * @param dc The <code>DbControl</code> object used for database access
	 *        and permission checking
	 * @param id The id of the item
	 * @return An item of the type represented by this enumeration constant
	 * @throws ItemNotFoundException If an item with the specified id isn't
	 *         found
	 * @throws PermissionDeniedException If the logged in user hasn't read
	 *         permission for the item
	 * @throws BaseException If there is another error
	 */
//	@SuppressWarnings("unchecked")
//	public BasicItem getById(DbControl dc, int id)
//			throws ItemNotFoundException, PermissionDeniedException,
//			BaseException
//	{
//		if (getById == null)
//		{
//			throw new ItemNotFoundException(itemClass + "[id=" + id + "]");
//		}
//		try
//		{
//			return (BasicItem) getById.invoke(null, dc, id);
//		}
//		catch (Throwable ex)
//		{
//			Throwable cause = ex.getCause();
//			if (cause instanceof BaseException)
//			{
//				throw (BaseException) cause;
//			}
//			else if (cause != null)
//			{
//				throw new BaseException(cause);
//			}
//			else
//			{
//				throw new BaseException(ex);
//			}
//		}
//	}


	/**
	 * Get the default constructor for new objects of this type. The default
	 * constructor is a constructor that takes a <code>BasicData</code> object
	 * as the only parameter. For example <code>Sample(SampleData data)</code>.
	 * 
	 * @return The constructor, or null if no such constructor exists
	 * @see DbControl#getItem(Class, BasicData, Object[])
	 */
	@SuppressWarnings("unchecked")
	Constructor<? extends BasicItem> getConstructor()
	{
		return constructor;
	}


	/**
	 * Get the <code>Item</code> object when you know the integer code.
	 */
	public static Item fromValue(int value)
	{
		Item item = valueMapping.get(value);
		assert item != null : "item == null for value " + value;
		return item;
	}


	/**
	 * Get the <code>Item</code> object when you know the item class.
	 */
	@SuppressWarnings("unchecked")
	public static Item fromItemClass(Class<? extends BasicItem> itemClass)
	{
		Item item = itemClassMapping.get(itemClass);
		assert item != null : "item == null for itemClass " + itemClass;
		return item;
	}


	/**
	 * Get the <code>Item</code> object when you know the data class.
	 */
	public static Item fromDataClass(Class<? extends BasicData> dataClass)
	{
		Item item = dataClassMapping.get(dataClass);
		assert item != null : "item == null for dataClass " + dataClass;
		return item;
	}


	/**
	 * Get the <code>Item</code> object when you know either the item or data
	 * class.
	 * 
	 * @return The Item object, or null if no matching item is found
	 */
	@SuppressWarnings(
		{
			"unchecked"
		})
	public static Item fromClass(Class<?> anyClass)
	{
		Item item = null;
		if (BasicItem.class.isAssignableFrom(anyClass))
		{
			item = fromItemClass((Class<BasicItem>) anyClass);
		}
		else if (BasicData.class.isAssignableFrom(anyClass))
		{
			item = fromDataClass((Class<BasicData>) anyClass);
		}
		return item;
	}


	/**
	 * Get the <code>Item</code> object when you have a data layer object.
	 * This method takes Hibernate proxies into account.
	 */
	@SuppressWarnings(
		{
			"unchecked"
		})
	public static Item fromDataObject(BasicData data)
	{
		Class<? extends BasicData> dataClass = data.getClass();
//		if (data instanceof org.hibernate.proxy.HibernateProxy)
//		{
//			dataClass = org.hibernate.proxy.HibernateProxyHelper
//				.getClassWithoutInitializingProxy(data);
//		}
		return fromDataClass(dataClass);
	}

	/**
	 * Helper class to make it easier to set up the defined permissions.
	 * 
	 * @see #getDefinedPermissions()
	 */
	private static class DefinedPermissions
	{
		private static final Set<Permission> read = Collections
			.unmodifiableSet(EnumSet.of(Permission.READ));
		private static final Set<Permission> write = Collections
			.unmodifiableSet(EnumSet.of(Permission.READ, Permission.USE,
				Permission.WRITE));
		private static final Set<Permission> basic = Collections
			.unmodifiableSet(EnumSet.of(Permission.CREATE, Permission.READ,
				Permission.USE, Permission.WRITE, Permission.DELETE));
		private static final Set<Permission> ownable = Collections
			.unmodifiableSet(EnumSet.of(Permission.CREATE, Permission.READ,
				Permission.USE, Permission.WRITE, Permission.DELETE,
				Permission.SET_OWNER));
		private static final Set<Permission> shareable = Collections
			.unmodifiableSet(EnumSet.of(Permission.CREATE, Permission.READ,
				Permission.USE, Permission.WRITE, Permission.DELETE,
				Permission.SET_OWNER, Permission.SET_PERMISSION));
	}
}
