package com.sortmaster.model;

import java.util.List;
import java.util.Map;

public class AlgorithmMetadata {
    private String id;
    private String name;
    private String category;
    private String shortDescription;
    private String detailedDescription;
    private String bestTimeComplexity;
    private String averageTimeComplexity;
    private String worstTimeComplexity;
    private String spaceComplexity;
    private boolean stable;
    private boolean inPlace;
    private String difficulty;
    private List<String> recommendedUseCases;
    private List<String> advantages;
    private List<String> disadvantages;
    private List<String> commonMistakes;
    private List<String> examQuestions;
    private String pseudocode;
    private String javaCode;
    private String pythonCode;
    private String pseudocodeDescending;
    private String javaCodeDescending;
    private String pythonCodeDescending;
    private Map<String, String> conceptualLineExplanations;

    public AlgorithmMetadata() {
    }

    public AlgorithmMetadata(String id,
                             String name,
                             String category,
                             String shortDescription,
                             String detailedDescription,
                             String bestTimeComplexity,
                             String averageTimeComplexity,
                             String worstTimeComplexity,
                             String spaceComplexity,
                             boolean stable,
                             boolean inPlace,
                             String difficulty,
                             List<String> recommendedUseCases,
                             List<String> advantages,
                             List<String> disadvantages,
                             List<String> commonMistakes,
                             List<String> examQuestions,
                             String pseudocode,
                             String javaCode,
                             String pythonCode,
                             String pseudocodeDescending,
                             String javaCodeDescending,
                             String pythonCodeDescending,
                             Map<String, String> conceptualLineExplanations) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.shortDescription = shortDescription;
        this.detailedDescription = detailedDescription;
        this.bestTimeComplexity = bestTimeComplexity;
        this.averageTimeComplexity = averageTimeComplexity;
        this.worstTimeComplexity = worstTimeComplexity;
        this.spaceComplexity = spaceComplexity;
        this.stable = stable;
        this.inPlace = inPlace;
        this.difficulty = difficulty;
        this.recommendedUseCases = recommendedUseCases;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.commonMistakes = commonMistakes;
        this.examQuestions = examQuestions;
        this.pseudocode = pseudocode;
        this.javaCode = javaCode;
        this.pythonCode = pythonCode;
        this.pseudocodeDescending = pseudocodeDescending;
        this.javaCodeDescending = javaCodeDescending;
        this.pythonCodeDescending = pythonCodeDescending;
        this.conceptualLineExplanations = conceptualLineExplanations;
    }

    public AlgorithmMetadata(String id,
                             String name,
                             String category,
                             String shortDescription,
                             String detailedDescription,
                             String bestTimeComplexity,
                             String averageTimeComplexity,
                             String worstTimeComplexity,
                             String spaceComplexity,
                             boolean stable,
                             boolean inPlace,
                             String difficulty,
                             List<String> recommendedUseCases,
                             List<String> advantages,
                             List<String> disadvantages,
                             List<String> commonMistakes,
                             List<String> examQuestions,
                             String pseudocode,
                             String javaCode,
                             String pythonCode,
                             Map<String, String> conceptualLineExplanations) {
        this(id, name, category, shortDescription, detailedDescription,
             bestTimeComplexity, averageTimeComplexity, worstTimeComplexity, spaceComplexity,
             stable, inPlace, difficulty, recommendedUseCases, advantages, disadvantages,
             commonMistakes, examQuestions, pseudocode, javaCode, pythonCode,
             null, null, null, conceptualLineExplanations);
    }

    public AlgorithmMetadata(String id,
                             String name,
                             String category,
                             String shortDescription,
                             String detailedDescription,
                             String bestTimeComplexity,
                             String averageTimeComplexity,
                             String worstTimeComplexity,
                             String spaceComplexity,
                             boolean stable,
                             boolean inPlace,
                             String difficulty,
                             List<String> recommendedUseCases,
                             List<String> advantages,
                             List<String> disadvantages,
                             List<String> commonMistakes,
                             List<String> examQuestions,
                             String pseudocode,
                             String javaCode,
                             Map<String, String> conceptualLineExplanations) {
        this(id, name, category, shortDescription, detailedDescription,
             bestTimeComplexity, averageTimeComplexity, worstTimeComplexity, spaceComplexity,
             stable, inPlace, difficulty, recommendedUseCases, advantages, disadvantages,
             commonMistakes, examQuestions, pseudocode, javaCode, "",
             null, null, null, conceptualLineExplanations);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getBestTimeComplexity() {
        return bestTimeComplexity;
    }

    public void setBestTimeComplexity(String bestTimeComplexity) {
        this.bestTimeComplexity = bestTimeComplexity;
    }

    public String getAverageTimeComplexity() {
        return averageTimeComplexity;
    }

    public void setAverageTimeComplexity(String averageTimeComplexity) {
        this.averageTimeComplexity = averageTimeComplexity;
    }

    public String getWorstTimeComplexity() {
        return worstTimeComplexity;
    }

    public void setWorstTimeComplexity(String worstTimeComplexity) {
        this.worstTimeComplexity = worstTimeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public boolean isInPlace() {
        return inPlace;
    }

    public void setInPlace(boolean inPlace) {
        this.inPlace = inPlace;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getRecommendedUseCases() {
        return recommendedUseCases;
    }

    public void setRecommendedUseCases(List<String> recommendedUseCases) {
        this.recommendedUseCases = recommendedUseCases;
    }

    public List<String> getAdvantages() {
        return advantages;
    }

    public void setAdvantages(List<String> advantages) {
        this.advantages = advantages;
    }

    public List<String> getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(List<String> disadvantages) {
        this.disadvantages = disadvantages;
    }

    public List<String> getCommonMistakes() {
        return commonMistakes;
    }

    public void setCommonMistakes(List<String> commonMistakes) {
        this.commonMistakes = commonMistakes;
    }

    public List<String> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<String> examQuestions) {
        this.examQuestions = examQuestions;
    }

    public String getPseudocode() {
        return pseudocode;
    }

    public void setPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }

    public String getPythonCode() {
        return pythonCode;
    }

    public void setPythonCode(String pythonCode) {
        this.pythonCode = pythonCode;
    }

    public String getPseudocodeDescending() {
        return pseudocodeDescending != null ? pseudocodeDescending : pseudocode;
    }

    public void setPseudocodeDescending(String pseudocodeDescending) {
        this.pseudocodeDescending = pseudocodeDescending;
    }

    public String getJavaCodeDescending() {
        return javaCodeDescending != null ? javaCodeDescending : javaCode;
    }

    public void setJavaCodeDescending(String javaCodeDescending) {
        this.javaCodeDescending = javaCodeDescending;
    }

    public String getPythonCodeDescending() {
        return pythonCodeDescending != null ? pythonCodeDescending : pythonCode;
    }

    public void setPythonCodeDescending(String pythonCodeDescending) {
        this.pythonCodeDescending = pythonCodeDescending;
    }

    public String getPseudocode(SortOrder order) {
        return (order == SortOrder.DESCENDING && pseudocodeDescending != null) ? pseudocodeDescending : pseudocode;
    }

    public String getJavaCode(SortOrder order) {
        return (order == SortOrder.DESCENDING && javaCodeDescending != null) ? javaCodeDescending : javaCode;
    }

    public String getPythonCode(SortOrder order) {
        return (order == SortOrder.DESCENDING && pythonCodeDescending != null) ? pythonCodeDescending : pythonCode;
    }

    public Map<String, String> getConceptualLineExplanations() {
        return conceptualLineExplanations;
    }

    public void setConceptualLineExplanations(Map<String, String> conceptualLineExplanations) {
        this.conceptualLineExplanations = conceptualLineExplanations;
    }
}
