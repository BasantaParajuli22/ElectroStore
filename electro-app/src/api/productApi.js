const API_BASE_URL = 'http://localhost:8080/api/products';

export const fetchProducts = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}`);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    return data.map(product => ({
      ...product,
      id: Number(product.id) // Force conversion to number
    }));
  } catch (error) {
    console.error('Error fetching products:', error.message);
    throw error;
  }
};


export const fetchProduct = async (id) => {
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching products:', error.message);
    throw error;
  }
};

export const createProduct = async (formData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/create`, {
      method: 'POST',
      body: formData //sending data as same format as form type of "multipart/form-data".

    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to add product');
    }
    return await response.json();
  } catch (error) {
    console.error(' error while adding product:', error.message);
    throw error;
  }
};

export const createProductNoFile = async (productData) => {
  try {
    const response = await fetch(`${API_BASE_URL}`, {
      method: 'POST',
     headers:{
      'Content-Type' : 'application/json'
     },
     body: JSON.stringify(productData)//converting to json to send to backend
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to add product');
    }
    return await response.json();
  } catch (error) {
    console.error(' error while adding product:', error.message);
    throw error;
  }
};

export const updateProduct = async (productData, id) => {
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
    },
      body: JSON.stringify(productData),
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update product');
    }
    return await response.json();
  } catch (error) {
    console.error('Full error:', error.message);
    throw error;
  }
};

export const deleteProduct = async ( id ) =>{
  try {
      const response = await fetch(`${API_BASE_URL}/${id}`,{ method : 'DELETE' });
      if(!response.ok){
        if(response.status === 404){
          throw new Error("product not found to delete");
        }
        throw new Error("server not found ");
      }
      return true; // Indicate success
  } catch (error) {
    console.log(`error while deleting ${error.message}`);
    throw error;
  }
}