use jni::JNIEnv;
use jni::objects::JClass;
use jni::sys::jstring;
use vulkano::instance::{Instance, InstanceCreateInfo};
use vulkano::VulkanLibrary;

#[unsafe(no_mangle)]
pub extern "system" fn Java_top_kmiit_debughelper_utils_GpuUtils_getVulkanVersionNative<'local>(
    env: JNIEnv<'local>,
    _class: JClass<'local>,
) -> jstring {
    let version_str = match get_physical_device_version() {
        Ok(v) => v,
        Err(_) => "unknown".to_string(),
    };

    let output = env.new_string(version_str).expect("Couldn't create java string!");
    output.into_raw()
}

fn get_physical_device_version() -> Result<String, Box<dyn std::error::Error>> {
    let library = VulkanLibrary::new()?;
    let instance = Instance::new(library, InstanceCreateInfo::default())?;

    // Get the first physical device
    let physical = instance
        .enumerate_physical_devices()?
        .next()
        .ok_or("No physical device found")?;

    let version = physical.properties().api_version;

    // Return formatted version: Major.Minor.Patch
    Ok(format!("{}.{}.{}", version.major, version.minor, version.patch))
}
