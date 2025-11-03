<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<div class="container">
    <h1 class="page-title">${empty recipe ? 'Add New Recipe' : 'Edit Recipe'}</h1>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/recipes" 
          enctype="multipart/form-data" class="recipe-form">
        <input type="hidden" name="action" value="save">
        <c:if test="${not empty recipe}">
            <input type="hidden" name="id" value="${recipe.id}">
        </c:if>

        <div class="form-section">
            <h2>Basic Information</h2>
            
            <div class="form-group">
                <label for="title">Recipe Title *</label>
                <input type="text" id="title" name="title" required 
                       value="${recipe.title}" placeholder="Enter recipe title">
            </div>

            <div class="form-group">
                <label for="description">Description *</label>
                <textarea id="description" name="description" rows="4" required 
                          placeholder="Describe your recipe...">${recipe.description}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="category">Category *</label>
                    <select id="category" name="category" required>
                        <option value="">Select category</option>
                        <option value="Vegetarian" ${recipe.category == 'Vegetarian' ? 'selected' : ''}>Vegetarian</option>
                        <option value="Vegan" ${recipe.category == 'Vegan' ? 'selected' : ''}>Vegan</option>
                        <option value="Dessert" ${recipe.category == 'Dessert' ? 'selected' : ''}>Dessert</option>
                        <option value="Beverage" ${recipe.category == 'Beverage' ? 'selected' : ''}>Beverage</option>
                        <option value="Main Course" ${recipe.category == 'Main Course' ? 'selected' : ''}>Main Course</option>
                        <option value="Appetizer" ${recipe.category == 'Appetizer' ? 'selected' : ''}>Appetizer</option>
                        <option value="Salad" ${recipe.category == 'Salad' ? 'selected' : ''}>Salad</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="cuisineType">Cuisine Type *</label>
                    <select id="cuisineType" name="cuisineType" required>
                        <option value="">Select cuisine</option>
                        <option value="Italian" ${recipe.cuisineType == 'Italian' ? 'selected' : ''}>Italian</option>
                        <option value="Mexican" ${recipe.cuisineType == 'Mexican' ? 'selected' : ''}>Mexican</option>
                        <option value="Indian" ${recipe.cuisineType == 'Indian' ? 'selected' : ''}>Indian</option>
                        <option value="Chinese" ${recipe.cuisineType == 'Chinese' ? 'selected' : ''}>Chinese</option>
                        <option value="American" ${recipe.cuisineType == 'American' ? 'selected' : ''}>American</option>
                        <option value="French" ${recipe.cuisineType == 'French' ? 'selected' : ''}>French</option>
                        <option value="Other" ${recipe.cuisineType == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="difficultyLevel">Difficulty *</label>
                    <select id="difficultyLevel" name="difficultyLevel" required>
                        <option value="">Select difficulty</option>
                        <option value="Easy" ${recipe.difficultyLevel == 'Easy' ? 'selected' : ''}>Easy</option>
                        <option value="Medium" ${recipe.difficultyLevel == 'Medium' ? 'selected' : ''}>Medium</option>
                        <option value="Hard" ${recipe.difficultyLevel == 'Hard' ? 'selected' : ''}>Hard</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="preparationTime">Preparation Time (minutes) *</label>
                    <input type="number" id="preparationTime" name="preparationTime" 
                           min="0" required value="${recipe.preparationTime}">
                </div>

                <div class="form-group">
                    <label for="cookingTime">Cooking Time (minutes) *</label>
                    <input type="number" id="cookingTime" name="cookingTime" 
                           min="0" required value="${recipe.cookingTime}">
                </div>

                <div class="form-group">
                    <label for="servings">Servings *</label>
                    <input type="number" id="servings" name="servings" 
                           min="1" required value="${recipe.servings}">
                </div>
            </div>

            <div class="form-group">
                <label for="tags">Tags (comma-separated)</label>
                <input type="text" id="tags" name="tags" 
                       value="<c:if test='${not empty recipe.tags}'><c:forEach var='tag' items='${recipe.tags}' varStatus='status'>${tag}<c:if test='${!status.last}'>, </c:if></c:forEach></c:if>"
                       placeholder="e.g., spicy, quick, healthy">
            </div>

            <div class="form-group">
                <label for="photo">Recipe Photo</label>
                <input type="file" id="photo" name="photo" accept="image/*">
                <c:if test="${not empty recipe.photoPath}">
                    <p>Current photo: <a href="${pageContext.request.contextPath}/${recipe.photoPath}" target="_blank">View</a></p>
                </c:if>
            </div>
        </div>

        <div class="form-section">
            <h2>Ingredients</h2>
            <div id="ingredients-container">
                <c:choose>
                    <c:when test="${not empty recipe and not empty recipe.ingredients}">
                        <c:forEach var="ingredient" items="${recipe.ingredients}" varStatus="status">
                            <div class="ingredient-item">
                                <input type="text" name="ingredientName" 
                                       value="${ingredient.name}" placeholder="Ingredient name" required>
                                <input type="number" name="ingredientQuantity" step="0.1" 
                                       value="${ingredient.quantity}" placeholder="Quantity" required>
                                <select name="ingredientUnit">
                                    <option value="cup" ${ingredient.unit == 'cup' ? 'selected' : ''}>cup</option>
                                    <option value="tbsp" ${ingredient.unit == 'tbsp' ? 'selected' : ''}>tbsp</option>
                                    <option value="tsp" ${ingredient.unit == 'tsp' ? 'selected' : ''}>tsp</option>
                                    <option value="oz" ${ingredient.unit == 'oz' ? 'selected' : ''}>oz</option>
                                    <option value="lb" ${ingredient.unit == 'lb' ? 'selected' : ''}>lb</option>
                                    <option value="g" ${ingredient.unit == 'g' ? 'selected' : ''}>g</option>
                                    <option value="kg" ${ingredient.unit == 'kg' ? 'selected' : ''}>kg</option>
                                    <option value="piece" ${ingredient.unit == 'piece' ? 'selected' : ''}>piece</option>
                                    <option value="" ${ingredient.unit == '' ? 'selected' : ''}>-</option>
                                </select>
                                <button type="button" class="btn btn-danger btn-sm" onclick="removeIngredient(this)">Remove</button>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="ingredient-item">
                            <input type="text" name="ingredientName" placeholder="Ingredient name" required>
                            <input type="number" name="ingredientQuantity" step="0.1" placeholder="Quantity" required>
                            <select name="ingredientUnit">
                                <option value="cup">cup</option>
                                <option value="tbsp">tbsp</option>
                                <option value="tsp">tsp</option>
                                <option value="oz">oz</option>
                                <option value="lb">lb</option>
                                <option value="g">g</option>
                                <option value="kg">kg</option>
                                <option value="piece">piece</option>
                                <option value="">-</option>
                            </select>
                            <button type="button" class="btn btn-danger btn-sm" onclick="removeIngredient(this)">Remove</button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <button type="button" class="btn btn-secondary" onclick="addIngredient()">+ Add Ingredient</button>
        </div>

        <div class="form-section">
            <h2>Preparation Steps</h2>
            <div id="steps-container">
                <c:choose>
                    <c:when test="${not empty recipe and not empty recipe.preparationSteps}">
                        <c:forEach var="step" items="${recipe.preparationSteps}">
                            <div class="step-item">
                                <textarea name="step" rows="2" placeholder="Enter step description..." required>${step}</textarea>
                                <button type="button" class="btn btn-danger btn-sm" onclick="removeStep(this)">Remove</button>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="step-item">
                            <textarea name="step" rows="2" placeholder="Enter step description..." required></textarea>
                            <button type="button" class="btn btn-danger btn-sm" onclick="removeStep(this)">Remove</button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <button type="button" class="btn btn-secondary" onclick="addStep()">+ Add Step</button>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Recipe</button>
            <a href="${pageContext.request.contextPath}/recipes" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<script>
function addIngredient() {
    const container = document.getElementById('ingredients-container');
    const div = document.createElement('div');
    div.className = 'ingredient-item';
    div.innerHTML = `
        <input type="text" name="ingredientName" placeholder="Ingredient name" required>
        <input type="number" name="ingredientQuantity" step="0.1" placeholder="Quantity" required>
        <select name="ingredientUnit">
            <option value="cup">cup</option>
            <option value="tbsp">tbsp</option>
            <option value="tsp">tsp</option>
            <option value="oz">oz</option>
            <option value="lb">lb</option>
            <option value="g">g</option>
            <option value="kg">kg</option>
            <option value="piece">piece</option>
            <option value="">-</option>
        </select>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeIngredient(this)">Remove</button>
    `;
    container.appendChild(div);
}

function removeIngredient(btn) {
    btn.parentElement.remove();
}

function addStep() {
    const container = document.getElementById('steps-container');
    const div = document.createElement('div');
    div.className = 'step-item';
    div.innerHTML = `
        <textarea name="step" rows="2" placeholder="Enter step description..." required></textarea>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeStep(this)">Remove</button>
    `;
    container.appendChild(div);
}

function removeStep(btn) {
    btn.parentElement.remove();
}
</script>

<%@ include file="footer.jsp" %>

