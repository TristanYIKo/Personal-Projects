import os
import shutil

downloads_dir = os.path.expanduser("~/Downloads")

file_type_folders = {
    "Documents": [".pdf", ".docx", ".doc", ".txt", ".xlsx", ".xls", ".pptx", ".ppt", ".csv", ".html"],
    "Images": [".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".svg", ".webp"],
    "Audio": [".mp3", ".wav", ".aac", ".flac", ".ogg", ".m4a"],
    "Videos": [".mp4", ".mkv", ".avi", ".mov", ".wmv", ".flv", ".webm"],
    "Archives": [".zip", ".rar", ".tar", ".tar.gz", ".7z", ".gz", ".bz2"],
    "Installers": [".exe", ".msi", ".pkg", ".dmg", ".deb", ".rpm"],
    "Code": [".py", ".java", ".c", ".cpp", ".js", ".html", ".css", ".php", ".rb", ".swift"],
    "Other": []  
}

def get_unique_filename(destination_folder, filename):
    base, extension = os.path.splitext(filename)
    counter = 1
    new_filename = filename

    while os.path.exists(os.path.join(destination_folder, new_filename)):
        new_filename = f"{base} ({counter}){extension}"
        counter += 1

    return new_filename

def create_and_move(file, folder_name, subfolder_name):
    folder_path = os.path.join(downloads_dir, folder_name)
    os.makedirs(folder_path, exist_ok=True)  
    
    subfolder_path = os.path.join(folder_path, subfolder_name)
    os.makedirs(subfolder_path, exist_ok=True)  
    
    target_path = os.path.join(subfolder_path, file)
    unique_filename = get_unique_filename(subfolder_path, file)
    final_path = os.path.join(subfolder_path, unique_filename)
    
    try:
        shutil.move(os.path.join(downloads_dir, file), final_path)
        print(f"Moved {file} to {final_path}")
    except Exception as e:
        print(f"Error moving {file} to {final_path}: {e}")

def sort_downloads():
    for file in os.listdir(downloads_dir):
        file_path = os.path.join(downloads_dir, file)
        
        if os.path.isfile(file_path):
            file_ext = os.path.splitext(file)[1].lower() 
            
            destination_folder = "Other"
            destination_subfolder = "Uncategorized"
            
            for folder_name, extensions in file_type_folders.items():
                if file_ext in extensions:
                    destination_folder = folder_name
                    destination_subfolder = file_ext.lstrip('.') 
                    break
            
            create_and_move(file, destination_folder, destination_subfolder)

if __name__ == "__main__":
    sort_downloads()  
